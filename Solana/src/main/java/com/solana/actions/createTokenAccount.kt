package com.solana.actions

import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.models.REQUIRED_ACCOUNT_SPACE
import com.solana.programs.SystemProgram
import com.solana.programs.TokenProgram
import com.solana.api.getMinimumBalanceForRentExemption
import com.solana.api.sendTransaction

fun Action.createTokenAccount(
    account: Account,
    mintAddress: PublicKey,
    onComplete: ((Result<Pair<String, PublicKey>>) -> Unit)
) {
    api.getMinimumBalanceForRentExemption(REQUIRED_ACCOUNT_SPACE){
        it.onSuccess { balance ->
            val transaction = Transaction()
            val newAccount = Account()
            val createAccountInstruction = SystemProgram.createAccount(
                account.publicKey,
                newAccount.publicKey,
                balance,
                REQUIRED_ACCOUNT_SPACE,
                TokenProgram.PROGRAM_ID
            )

            transaction.addInstruction(createAccountInstruction)
            val initializeAccountInstruction = TokenProgram.initializeAccount(
                newAccount.publicKey,
                mintAddress,
                account.publicKey
            )

            transaction.addInstruction(initializeAccountInstruction)
            api.sendTransaction(transaction, listOf(newAccount, account), null){ result ->
                result.onSuccess { transactionId ->
                    onComplete(Result.success(Pair(transactionId, newAccount.publicKey)))
                }.onFailure { error ->
                    onComplete(Result.failure(error))
                }
            }
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}