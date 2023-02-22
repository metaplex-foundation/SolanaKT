package com.solana.actions

import com.solana.api.sendTransaction
import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.programs.SystemProgram
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Action.sendSOL(
    account: Account,
    destination: PublicKey,
    amount: Long,
    onComplete: ((Result<String>) -> Unit)
) {
    CoroutineScope(dispatcher).launch {
        onComplete(sendSOL(account, destination, amount))
    }
}

suspend fun Action.sendSOL(
    account: Account,
    destination: PublicKey,
    amount: Long
): Result<String> {
    val instructions = SystemProgram.transfer(account.publicKey, destination, amount)
    val transaction = Transaction()
    transaction.add(instructions)
    val transactionId = api.sendTransaction(transaction, listOf(account)).getOrElse {
        return Result.failure(it)
    }
    return Result.success(transactionId)
}