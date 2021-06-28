package com.solana.api

import com.solana.core.PublicKey

fun Api.getSlotLeader(onComplete: (Result<PublicKey>) -> Unit) {
    router.call("getSlotLeader", ArrayList(), String::class.java) { result ->
        result.map {
            PublicKey(it)
        }.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}