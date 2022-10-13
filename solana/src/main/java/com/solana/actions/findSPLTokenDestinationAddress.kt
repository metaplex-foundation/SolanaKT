package com.solana.actions

import com.solana.api.getAccountInfo
import com.solana.api.getSplTokenAccountInfo
import com.solana.api.nullValueError
import com.solana.core.PublicKey
import com.solana.core.PublicKey.Companion.createProgramAddress
import com.solana.models.buffer.AccountInfo
import com.solana.models.buffer.BufferInfo
import com.solana.programs.SystemProgram
import com.solana.programs.TokenProgram
import com.solana.vendor.ResultError
import com.solana.vendor.ContResult
import com.solana.vendor.Result
import com.solana.vendor.flatMap

typealias SPLTokenDestinationAddress = Pair<PublicKey,Boolean>

fun Action.findSPLTokenDestinationAddress(
    mintAddress: PublicKey,
    destinationAddress: PublicKey,
    allowUnfundedRecipient: Boolean = false,
    onComplete: ((Result<SPLTokenDestinationAddress, ResultError>) -> Unit)
){
    if(allowUnfundedRecipient) {
        checkSPLTokenAccountExistence(
            mintAddress,
            destinationAddress,
            onComplete
        )
    } else {
        findSPLTokenDestinationAddressOfExistingAccount(
            mintAddress,
            destinationAddress,
            onComplete
        )
    }
}

private fun Action.checkSPLTokenAccountExistence(
    mintAddress: PublicKey,
    destinationAddress: PublicKey,
    onComplete: ((Result<SPLTokenDestinationAddress, ResultError>) -> Unit)
) {

    var associatedTokenAddress: PublicKey? = null
    try {
        val associatedProgramDerivedAddress = PublicKey.associatedTokenAddress(destinationAddress, mintAddress).address
        associatedTokenAddress = associatedProgramDerivedAddress
    } catch (error: Exception){
        onComplete(Result.failure(error))
    }

    var hasAssociatedTokenAccount = false
    associatedTokenAddress?.let {
        this.api.getSplTokenAccountInfo(it) { result ->
            result.onSuccess {
                hasAssociatedTokenAccount = true
            }.onFailure { error ->
                if(error == nullValueError){
                    hasAssociatedTokenAccount = false
                } else {
                    onComplete(Result.failure(error.message!!))
                    return@onFailure
                }
            }
            onComplete(Result.success(SPLTokenDestinationAddress(associatedTokenAddress, !hasAssociatedTokenAccount)))
        }
    }
}

private fun Action.findSPLTokenDestinationAddressOfExistingAccount(
    mintAddress: PublicKey,
    destinationAddress: PublicKey,
    onComplete: ((Result<SPLTokenDestinationAddress, ResultError>) -> Unit)
){
    return ContResult<BufferInfo<AccountInfo>, ResultError> { cb ->
        this.api.getAccountInfo(
            destinationAddress,
            AccountInfo::class.java
        ) { result ->
            result.onSuccess {
                cb(Result.success(it))
            }.onFailure {
                cb(Result.failure(it))
            }
        }
    }.flatMap { info ->
        val toTokenMint = info.data?.value?.mint?.toBase58()
        var toPublicKeyString = ""
        if (mintAddress.toBase58() == toTokenMint) {
            // detect if destination address is already a SPLToken address
            toPublicKeyString = destinationAddress.toBase58()
        }else if (info.owner == SystemProgram.PROGRAM_ID.toBase58()) {
            // detect if destination address is a SOL address
            val address = createProgramAddress(
                listOf(destinationAddress.toByteArray()),
                mintAddress
            )
            toPublicKeyString = address.toBase58()
        }
        val toPublicKey = PublicKey(toPublicKeyString)
        if(destinationAddress.toBase58() != toPublicKey.toBase58()){
            ContResult<BufferInfo<AccountInfo>, ResultError> { cb ->
                this.api.getAccountInfo(
                    toPublicKey,
                    AccountInfo::class.java
                ){ result ->
                    result.onSuccess {
                        cb(Result.success(it))
                    }.onFailure {
                        cb(Result.failure(ResultError(it)))
                    }
                }
            }.map{ info1 ->
                var isUnregisteredAsocciatedToken = true
                // if associated token account has been registered
                if(info1.owner == TokenProgram.PROGRAM_ID.toBase58() &&
                    info.data?.value != null) {
                    isUnregisteredAsocciatedToken = false
                }
                SPLTokenDestinationAddress(toPublicKey,isUnregisteredAsocciatedToken)
            }
        } else {
            ContResult.success(SPLTokenDestinationAddress(toPublicKey, false))
        }
    }.run(onComplete)
}