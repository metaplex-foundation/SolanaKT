package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.TokenResultObjects

fun Api.getTokenSupply(tokenMint: PublicKey,
                   onComplete: (Result<TokenResultObjects.TokenAmountInfo>) -> Unit) {
    val params: MutableList<Any> = ArrayList()
    params.add(tokenMint.toString())
    router.request<Map<String, Any>>(
        "getTokenSupply",
        params,
        Map::class.java
    ){ result ->
        result.map {
            it["value"]  as Map<String, Any>
        }.map {
            TokenResultObjects.TokenAmountInfo(it)
        }.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}