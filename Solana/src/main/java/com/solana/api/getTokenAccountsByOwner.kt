package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.TokenAccountInfo

fun Api.getTokenAccountsByOwner(owner: PublicKey, tokenMint: PublicKey, onComplete: (Result<PublicKey>) -> Unit) {
    val params: MutableList<Any> = ArrayList()
    params.add(owner.toBase58())
    val parameterMap: MutableMap<String, Any> = HashMap()
    parameterMap["mint"] = tokenMint.toBase58()
    params.add(parameterMap)
    router.call(
        "getTokenAccountsByOwner", params,
        Map::class.java
    ) { result ->
        result.map {
            it as Map<String, Any>
        }.map {
            it["value"] as List<*>
        }.map {
            it[0] as Map<String, Any>
        }.map {
            it["pubkey"] as String
        }.map {
            PublicKey(it)
        }.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}

fun Api.getTokenAccountsByOwner(
    accountOwner: PublicKey, requiredParams: Map<String, Any>,
    optionalParams: Map<String, Any>?,
    onComplete: (Result<TokenAccountInfo>) -> Unit,
) {
    getTokenAccount(
        accountOwner,
        requiredParams,
        optionalParams,
        "getTokenAccountsByOwner",
        onComplete
    )
}