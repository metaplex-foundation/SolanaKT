package com.solana.actions

import com.solana.core.Account
import com.solana.core.HotAccount
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.programs.SystemProgram

fun Action.sendSOL(
    account: Account,
    destination: PublicKey,
    amount: Long,
    onComplete: ((Result<String>) -> Unit)
) {
    val instructions = SystemProgram.transfer(account.publicKey, destination, amount)
    val transaction = Transaction()
    transaction.add(instructions)
    this.serializeAndSendWithFee(transaction, listOf(account), null){ result ->
        result.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}