package com.solana.actions

import com.solana.api.getAccountInfo
import com.solana.api.getMultipleAccounts
import com.solana.core.PublicKey
import com.solana.models.buffer.BufferInfo
import com.solana.models.buffer.Mint
import com.solana.programs.TokenProgram
import com.solana.vendor.ContResult
import com.solana.vendor.Result
import com.solana.vendor.ResultError
import com.solana.vendor.flatMap
import java.lang.RuntimeException

fun Action.getMintData(
    mintAddress: PublicKey,
    programId: PublicKey = TokenProgram.PROGRAM_ID,
    onComplete: ((Result<Mint, Exception>) -> Unit)
){
    this.api.getAccountInfo(mintAddress, Mint::class.java) {
        it.onSuccess { account ->
            if (account.owner != programId.toBase58()) {
                onComplete(Result.failure(RuntimeException("Invalid mint owner")))
                return@getAccountInfo
            }

            account.data?.value?.let { mint ->
                onComplete(Result.success(mint))
                return@getAccountInfo
            }
            onComplete(Result.failure(RuntimeException("Invalid data")))
            return@getAccountInfo
        }.onFailure { error ->
            onComplete(Result.failure(RuntimeException(error)))
        }
    }
}

fun Action.getMultipleMintDatas(
    mintAddresses: List<PublicKey>,
    programId: PublicKey = TokenProgram.PROGRAM_ID,
    onComplete: ((Result<Map<PublicKey, Mint>, Exception>) -> Unit)
){
    ContResult<List<BufferInfo<Mint>>, ResultError> { cb ->
        this.api.getMultipleAccounts(mintAddresses, Mint::class.java) { result ->
            result.onSuccess {
                cb(Result.success(it))
            }.onFailure {
                cb(Result.failure(Exception(it)))
            }
        }
    }.flatMap { account ->
        if(account.find { it.owner == programId.toBase58()} == null) {
            return@flatMap ContResult.failure(ResultError("Invalid mint owner"))
        }
        val values = account.mapNotNull { it.data?.value }
        if(values.size != mintAddresses.size) {
            return@flatMap ContResult.failure(ResultError("Some of mint data are missing"))
        }

        val mintDict = mutableMapOf<PublicKey,Mint>()
        mintAddresses.forEachIndexed { index, address ->
            mintDict[address] = values[index]
        }
        return@flatMap ContResult.success(mintDict.toMap())
    }.run(onComplete)
}