package com.solana.actions

import com.solana.api.sendTransaction
import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.programs.TokenProgram

fun Action.closeTokenAccount(
    account: Account,
    tokenPubkey: PublicKey,
    onComplete: ((Result<Pair<String, PublicKey>>) -> Unit)
){
    val transaction = Transaction()
    val instruction = TokenProgram.closeAccount(
        account = tokenPubkey,
        destination = account.publicKey,
        owner= account.publicKey
    )
    transaction.addInstruction(instruction)
    api.sendTransaction(transaction, listOf(account)){ result ->
        result.onSuccess { transactionId ->
            onComplete(Result.success(Pair(transactionId, tokenPubkey)))
        }.onFailure { error ->
            onComplete(Result.failure(error))
        }
    }
}