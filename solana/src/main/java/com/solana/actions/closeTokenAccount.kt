package com.solana.actions

import com.solana.api.sendTransaction
import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.programs.TokenProgram
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Action.closeTokenAccount(
    account: Account,
    tokenPubkey: PublicKey,
    onComplete: ((Result<Pair<String, PublicKey>>) -> Unit)
){
    CoroutineScope(dispatcher).launch {
        onComplete(closeTokenAccount(account, tokenPubkey))
    }
}

suspend fun Action.closeTokenAccount(
    account: Account,
    tokenPubkey: PublicKey,
): Result<Pair<String, PublicKey>> {
    val transaction = Transaction()
    val instruction = TokenProgram.closeAccount(
        account = tokenPubkey,
        destination = account.publicKey,
        owner= account.publicKey
    )

    transaction.add(instruction)
    val transactionId = api.sendTransaction(transaction, listOf(account)).getOrElse {
        return Result.failure(it)
    }
    return Result.success(Pair(transactionId, tokenPubkey))
}