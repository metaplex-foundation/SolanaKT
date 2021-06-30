package com.solana.actions

import com.solana.api.getProgramAccounts
import com.solana.core.PublicKey
import com.solana.models.*
import com.solana.models.Buffer.AccountInfoLayout
import com.solana.models.Buffer.AccountInfoData
import com.solana.programs.TokenProgram
import com.solana.vendor.ContResult
import com.solana.vendor.Result


fun Action.getTokenWallets(
    account: PublicKey,
    onComplete: ((Result<Any, java.lang.Exception>) -> Unit)
) {
    val memcmp = listOf(
        Memcmp(32, account.toBase58())
    )
    ContResult<List<ProgramAccount<AccountInfoData>>, Exception> { cb ->
        api.getProgramAccounts(TokenProgram.PROGRAM_ID, memcmp, 165, AccountInfoData::class.java, AccountInfoLayout()) { result ->
            result.onSuccess {
                cb(Result.success(it))
            }.onFailure {
                cb(Result.failure(Exception(it)))
            }
        }
    }.map { accounts ->
        val accountsValues = accounts.map { if(it.account.data != null) { it } else { null } }.filterNotNull()
        val pubkeyValue = accountsValues.map { Pair(it.pubkey, it.account) }
        val wallets = pubkeyValue.map {
            val mintAddress = it.second.data!!.value!!.mint
            Wallet(it.first, it.second.lamports.toLong(), true)
        }
        return@map wallets
    }.run(onComplete)
}

