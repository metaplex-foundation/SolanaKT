package com.solana.actions

import com.solana.core.HotAccount
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.models.REQUIRED_ACCOUNT_SPACE
import com.solana.programs.SystemProgram
import com.solana.programs.TokenProgram
import com.solana.api.getMinimumBalanceForRentExemption
import com.solana.core.Account

fun Action.createTokenAccount(
    account: Account,
    mintAddress: PublicKey,
    onComplete: ((Result<Pair<String, PublicKey>>) -> Unit)
) {
    api.getMinimumBalanceForRentExemption(REQUIRED_ACCOUNT_SPACE){
        it.onSuccess { balance ->
            val transaction = Transaction()
            val newAccount = HotAccount()
            val createAccountInstruction = SystemProgram.createAccount(
                fromPublicKey = account.publicKey,
                newAccountPublickey = newAccount.publicKey,
                lamports = balance,
                REQUIRED_ACCOUNT_SPACE,
                TokenProgram.PROGRAM_ID
            )

            transaction.addInstruction(createAccountInstruction)
            val initializeAccountInstruction = TokenProgram.initializeAccount(
                account = newAccount.publicKey,
                mint = mintAddress,
                owner = account.publicKey
            )

            transaction.addInstruction(initializeAccountInstruction)
            this.serializeAndSendWithFee(transaction, listOf(account,newAccount), null){ result ->
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