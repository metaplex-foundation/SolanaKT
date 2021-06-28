package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.TokenAccountInfo

fun Api.getTokenAccountsByDelegate(
    accountDelegate: PublicKey,
    requiredParams: Map<String, Any>,
    optionalParams: Map<String, Any>?,
    onComplete: (Result<TokenAccountInfo>) -> Unit
) {
    return getTokenAccount(
        accountDelegate,
        requiredParams,
        optionalParams,
        "getTokenAccountsByDelegate",
        onComplete
    )
}