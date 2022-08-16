package com.solana.actions

import com.solana.core.Account
import com.solana.core.HotAccount
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.programs.AssociatedTokenProgram
import com.solana.programs.TokenProgram
import com.solana.vendor.ContResult
import com.solana.vendor.ResultError
import com.solana.vendor.Result
import com.solana.vendor.flatMap

fun Action.sendSPLTokens(
    mintAddress: PublicKey,
    fromPublicKey: PublicKey,
    destinationAddress: PublicKey,
    amount: Long,
    allowUnfundedRecipient: Boolean = false,
    account: Account,
    onComplete: ((Result<String, ResultError>) -> Unit)
){
    ContResult<SPLTokenDestinationAddress, ResultError> { cb ->
        this.findSPLTokenDestinationAddress(
            mintAddress,
            destinationAddress,
            allowUnfundedRecipient
        ) { cb(it) }
    }.flatMap { spl ->
        val toPublicKey = spl.first
        val isUnregisteredAsocciatedToken = spl.second
        if(fromPublicKey.toBase58() == toPublicKey.toBase58()){
            return@flatMap ContResult.failure(ResultError("Same send and destination address."))
        }

        val transaction = Transaction()

        // create associated token address
        if(isUnregisteredAsocciatedToken) {
            val mint = mintAddress
            val owner = destinationAddress
            val createATokenInstruction = AssociatedTokenProgram.createAssociatedTokenAccountInstruction(
                mint =  mint,
                associatedAccount = toPublicKey,
                owner = owner,
                payer = account.publicKey
            )
            transaction.addInstruction(createATokenInstruction)
        }

        // send instruction
        val sendInstruction = TokenProgram.transfer(fromPublicKey,toPublicKey, amount, account.publicKey)
        transaction.addInstruction(sendInstruction)
        return@flatMap ContResult.success(transaction)
    }.flatMap { transaction ->
        return@flatMap ContResult<String, ResultError> { cb ->
            this.serializeAndSendWithFee(transaction, listOf(account)) { result ->
                result.onSuccess {
                    cb(Result.success(it))
                }.onFailure {
                    cb(Result.failure(ResultError(it)))
                }
            }
        }
    }.run(onComplete)
}