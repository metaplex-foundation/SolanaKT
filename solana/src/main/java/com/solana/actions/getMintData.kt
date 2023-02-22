package com.solana.actions

import com.solana.api.SolanaAccountSerializer
import com.solana.api.getAccountInfo
import com.solana.api.getMultipleAccountsInfo
import com.solana.core.PublicKey
import com.solana.models.buffer.Mint
import com.solana.programs.TokenProgram
import com.solana.vendor.ResultError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.RuntimeException

fun Action.getMintData(
    mintAddress: PublicKey,
    programId: PublicKey = TokenProgram.PROGRAM_ID,
    onComplete: ((Result<Mint>) -> Unit)
){
    CoroutineScope(dispatcher).launch {
        onComplete(getMintData(mintAddress, programId))
    }
}

suspend fun Action.getMintData(
    mintAddress: PublicKey,
    programId: PublicKey = TokenProgram.PROGRAM_ID
): Result<Mint> {
    val account = this.api.getAccountInfo(SolanaAccountSerializer(Mint.serializer()), mintAddress).getOrElse {
        return Result.failure(it)
    }
    if (account?.owner != programId.toBase58()) {
        return Result.failure(RuntimeException("Invalid mint owner"))
    }
    account.data?.let {
        return Result.success(it)
    }.run {
        return Result.failure(RuntimeException("Invalid data"))
    }
}

fun Action.getMultipleMintDatas(
    mintAddresses: List<PublicKey>,
    programId: PublicKey = TokenProgram.PROGRAM_ID,
    onComplete: ((Result<Map<PublicKey, Mint>>) -> Unit)
){
    CoroutineScope(dispatcher).launch {
        onComplete(getMultipleMintDatas(mintAddresses, programId))
    }
}

suspend fun Action.getMultipleMintDatas(
    mintAddresses: List<PublicKey>,
    programId: PublicKey = TokenProgram.PROGRAM_ID
): Result<Map<PublicKey, Mint>> {
    val account = this.api.getMultipleAccountsInfo(Mint.serializer(), mintAddresses).getOrElse {
        return Result.failure(it)
    }
    if(account.find { it?.owner == programId.toBase58()} == null) {
        return Result.failure(ResultError("Invalid mint owner"))
    }
    val values = account.mapNotNull { it?.data }
    if(values.size != mintAddresses.size) {
        return Result.failure(ResultError("Some of mint data are missing"))
    }

    val mintDict = mutableMapOf<PublicKey,Mint>()
    mintAddresses.forEachIndexed { index, address ->
        mintDict[address] = values[index]
    }
    return Result.success(mintDict.toMap())
}