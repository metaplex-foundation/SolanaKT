package com.solana.api

import com.solana.core.PublicKey

fun Api.getIdentity(onComplete: (Result<PublicKey>) -> Unit) {
    router.call(
        "getIdentity", ArrayList(),
        MutableMap::class.java
    ) { result ->
        result.map {
            val base58 = it["identity"] as String
            PublicKey(base58)
        }.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}