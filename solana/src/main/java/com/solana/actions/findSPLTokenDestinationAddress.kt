package com.solana.actions

import com.solana.api.*
import com.solana.core.PublicKey
import com.solana.core.PublicKey.Companion.createProgramAddress
import com.solana.models.buffer.AccountInfoData
import com.solana.networking.serialization.serializers.base64.BorshAsBase64JsonArraySerializer
import com.solana.programs.SystemProgram
import com.solana.programs.TokenProgram
import com.solana.vendor.ResultError
import com.solana.vendor.ContResult
import com.solana.vendor.Result
import com.solana.vendor.flatMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

typealias SPLTokenDestinationAddress = Pair<PublicKey, Boolean>

suspend fun Action.findSPLTokenDestinationAddress(
    mintAddress: PublicKey,
    destinationAddress: PublicKey,
    allowUnfundedRecipient: Boolean = false,
): Result<SPLTokenDestinationAddress, ResultError> {
    return if(allowUnfundedRecipient) {
        checkSPLTokenAccountExistence(
            mintAddress,
            destinationAddress,
        )
    } else {
        findSPLTokenDestinationAddressOfExistingAccount(
            mintAddress,
            destinationAddress,
        )
    }
}

fun Action.findSPLTokenDestinationAddress(
    mintAddress: PublicKey,
    destinationAddress: PublicKey,
    allowUnfundedRecipient: Boolean = false,
    onComplete: ((Result<SPLTokenDestinationAddress, ResultError>) -> Unit)
){
    CoroutineScope(api.dispatcher).launch {
        onComplete(findSPLTokenDestinationAddress(mintAddress, destinationAddress, allowUnfundedRecipient))
    }
}

suspend fun Action.checkSPLTokenAccountExistence(
    mintAddress: PublicKey,
    destinationAddress: PublicKey
): Result<SPLTokenDestinationAddress, ResultError> {
    var associatedTokenAddress: PublicKey? = null
    try {
        val associatedProgramDerivedAddress = PublicKey.associatedTokenAddress(destinationAddress, mintAddress).address
        associatedTokenAddress = associatedProgramDerivedAddress
    } catch (error: Exception){
        return Result.failure(error)
    }

    var hasAssociatedTokenAccount = false
    val result = this.api.getSplTokenAccountInfo(associatedTokenAddress)
    result.onSuccess {
        hasAssociatedTokenAccount = true
    }.onFailure { error ->
        if(error.message == nullValueError.message){
            hasAssociatedTokenAccount = false
        } else {
            return Result.failure(ResultError(error))
        }
    }
    return Result.success(SPLTokenDestinationAddress(associatedTokenAddress, !hasAssociatedTokenAccount))
}

private fun Action.checkSPLTokenAccountExistence(
    mintAddress: PublicKey,
    destinationAddress: PublicKey,
    onComplete: ((Result<SPLTokenDestinationAddress, ResultError>) -> Unit)
) {
    CoroutineScope(api.dispatcher).launch {
        onComplete(checkSPLTokenAccountExistence(mintAddress, destinationAddress))
    }
}

suspend fun Action.findSPLTokenDestinationAddressOfExistingAccount(
    mintAddress: PublicKey,
    destinationAddress: PublicKey
): Result<SPLTokenDestinationAddress, ResultError> {
    val infoResult = this.api.getAccountInfo(
        AccountInfoSerializer(
            BorshAsBase64JsonArraySerializer((AccountInfoData.serializer()))),
        destinationAddress,
    )

    val info = infoResult.getOrElse {
        return Result.failure(Exception(it))
    }

    // Its en existing account
    val toTokenMint = info?.data?.mint?.toBase58()
    var toPublicKeyString = ""
    if (info?.owner == TokenProgram.PROGRAM_ID.toBase58() && mintAddress.toBase58() == toTokenMint) { // detect if destination address is already a SPLToken address
        toPublicKeyString = destinationAddress.toBase58()
    }else if (info?.owner == SystemProgram.PROGRAM_ID.toBase58()) { // detect if destination address is a SOL address
        val address = createProgramAddress(
            listOf(destinationAddress.toByteArray()),
            mintAddress
        )
        toPublicKeyString = address.toBase58()
    }
    val toPublicKey = PublicKey(toPublicKeyString)

    return if(destinationAddress.toBase58() != toPublicKey.toBase58()){
        val info1 = this.api.getAccountInfo(
            SolanaAccountSerializer(AccountInfoData.serializer()),
            toPublicKey
        ).getOrThrow()
        var isUnregisteredAsocciatedToken = true
        // if associated token account has been registered
        if(info1?.owner == TokenProgram.PROGRAM_ID.toBase58() &&
            info?.data != null) {
            isUnregisteredAsocciatedToken = false
        }
        Result.success(SPLTokenDestinationAddress(toPublicKey,isUnregisteredAsocciatedToken))
    } else {
        Result.success(SPLTokenDestinationAddress(toPublicKey, false))
    }
}