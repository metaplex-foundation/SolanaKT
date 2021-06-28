package com.solana.actions

import com.solana.api.Api
import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.programs.SystemProgram
import com.solana.programs.TokenProgram


fun Action.sendSOL(
    account: Account,
    destination: PublicKey,
    amount: Long,
    onComplete: ((Result<String>) -> Unit)
) {
    val instructions = SystemProgram.transfer(account.publicKey, destination, amount)
    val transaction = Transaction()
    transaction.addInstruction(instructions)
    api.sendTransaction(transaction, listOf(account), null){ result ->
        result.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}