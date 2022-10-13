package com.solana.actions

import com.solana.api.getProgramAccounts
import com.solana.core.PublicKey
import com.solana.models.*
import com.solana.models.buffer.AccountInfo
import com.solana.programs.TokenProgram

fun Action.getTokenWallets(
    account: PublicKey,
    onComplete: ((Result<List<Wallet>>) -> Unit)
) {
    val memcmp = listOf(
        Memcmp(32, account.toBase58())
    )
    api.getProgramAccounts(AccountInfo.serializer(), TokenProgram.PROGRAM_ID, memcmp, 165) { result ->
        result.onSuccess { accounts ->
            val accountsValues = accounts.map { if(it.account.data != null) { it } else { null } }.filterNotNull()
            val pubkeyValue = accountsValues.map { Pair(it.pubkey, it.account) }
            val wallets = pubkeyValue.map {
                val mintAddress = it.second.data!!.mint
                val token = this.supportedTokens.firstOrNull() { it.address == mintAddress.toBase58() } ?: Token.unsupported(mintAddress.toBase58())
                Wallet(it.first, it.second.lamports, token, true)
            }
            onComplete(Result.success(wallets))
        }.onFailure {
            onComplete(Result.failure(Exception(it)))
        }
    }
}

