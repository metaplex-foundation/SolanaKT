package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.TokenResultObjects

fun Api.getTokenLargestAccounts(tokenMint: PublicKey,
                            onComplete: (Result<List<TokenResultObjects.TokenAccount>>) -> Unit) {
    val params: MutableList<Any> = ArrayList()
    params.add(tokenMint.toString())
    router.call(
        "getTokenLargestAccounts", params,
        Map::class.java
    ){ result ->
        result.map {
            it["value"] as List<*>
        }.map {
            it.map { item -> item as Map<String, Any> }
        }.map {
            val list: MutableList<TokenResultObjects.TokenAccount> = ArrayList()
            for (item in (it)) {
                list.add(TokenResultObjects.TokenAccount(item))
            }
            list
        }.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}