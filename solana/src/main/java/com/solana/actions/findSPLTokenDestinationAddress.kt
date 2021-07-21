package com.solana.actions

import com.solana.api.getAccountInfo
import com.solana.core.PublicKey
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
                cb(Result.failure(Exception(it)))
            }
        }
    }.flatMap { info ->
        val toTokenMint = info.data?.value?.mint?.toBase58()
        var toPublicKeyString: String = ""
        if (mintAddress.toBase58() == toTokenMint) {
            // detect if destination address is already a SPLToken address
            toPublicKeyString = destinationAddress.toBase58()
        }else if (info.owner == SystemProgram.PROGRAM_ID.toBase58()) {
            // detect if destination address is a SOL address
            val owner = destinationAddress
            val tokenMint = mintAddress

            val address = PublicKey.createProgramAddress(
                listOf(owner.toByteArray()),
                tokenMint
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