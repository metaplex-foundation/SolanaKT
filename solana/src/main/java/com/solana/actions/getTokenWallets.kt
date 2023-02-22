package com.solana.actions

import com.solana.api.getProgramAccounts
import com.solana.core.PublicKey
import com.solana.models.*
import com.solana.models.buffer.AccountInfoData
import com.solana.programs.TokenProgram
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Action.getTokenWallets(
    account: PublicKey,
    onComplete: ((Result<List<Wallet>>) -> Unit)
) {
    CoroutineScope(dispatcher).launch {
        onComplete(getTokenWallets(account))
    }
}

suspend fun Action.getTokenWallets(
    account: PublicKey
) : Result<List<Wallet>>{

    val memcmp = listOf(
        Memcmp(32, account.toBase58())
    )

    val filters: MutableList<Any> = ArrayList()
    memcmp.forEach {
        filters.add(
            Filter(
                it
            )
        )
    }

    filters.add(DataSize(165.toLong()))
    val programAccountConfig = ProgramAccountConfig(filters = filters)
    val accounts = api.getProgramAccounts(AccountInfoData.serializer(), TokenProgram.PROGRAM_ID, programAccountConfig).getOrElse {
        return Result.failure(it)
    }

    val accountsValues = accounts.map { if(it.account.data != null) { it } else { null } }.filterNotNull()
    val pubkeyValue = accountsValues.map { Pair(it.publicKey, it.account) }
    val wallets = pubkeyValue.map {
        val mintAddress = it.second.data!!.mint
        val token = this.supportedTokens.firstOrNull() { it.address == mintAddress.toBase58() } ?: Token.unsupported(mintAddress.toBase58())
        Wallet(it.first, it.second.lamports, token, true)
    }
    return Result.success(wallets)
}