package com.solana.actions

import com.solana.api.SolanaAccountSerializer
import com.solana.api.getAccountInfo
import com.solana.api.getMultipleAccounts
import com.solana.core.PublicKey
import com.solana.models.buffer.Mint
import com.solana.programs.TokenProgram
import com.solana.vendor.ResultError
import java.lang.RuntimeException

fun Action.getMintData(
    mintAddress: PublicKey,
    programId: PublicKey = TokenProgram.PROGRAM_ID,
    onComplete: ((Result<Mint>) -> Unit)
){
    this.api.getAccountInfo(SolanaAccountSerializer(Mint.serializer()), mintAddress) { result ->
        result.onSuccess { account ->
                if (account?.owner != programId.toBase58()) {
                    onComplete(Result.failure(RuntimeException("Invalid mint owner")))
                }
                account?.data?.let { onComplete(Result.success(it)) }
                    .run { onComplete(Result.failure(RuntimeException("Invalid data"))) }
        }.onFailure { error ->
            onComplete(Result.failure(RuntimeException(error)))
        }
    }
}

fun Action.getMultipleMintDatas(
    mintAddresses: List<PublicKey>,
    programId: PublicKey = TokenProgram.PROGRAM_ID,
    onComplete: ((Result<Map<PublicKey, Mint>>) -> Unit)
){
    this.api.getMultipleAccounts(Mint.serializer(), mintAddresses) { result ->
        result.onSuccess { account ->
            if(account.find { it?.owner == programId.toBase58()} == null) {
                return@getMultipleAccounts onComplete(Result.failure(ResultError("Invalid mint owner")))
            }
            val values = account.mapNotNull { it?.data }
            if(values.size != mintAddresses.size) {
                return@getMultipleAccounts onComplete(Result.failure(ResultError("Some of mint data are missing")))
            }

            val mintDict = mutableMapOf<PublicKey,Mint>()
            mintAddresses.forEachIndexed { index, address ->
                mintDict[address] = values[index]
            }
            return@getMultipleAccounts onComplete(Result.success(mintDict.toMap()))
        }.onFailure {
            return@getMultipleAccounts onComplete(Result.failure(it))
        }
    }
}