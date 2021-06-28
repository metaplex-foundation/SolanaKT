package com.solana.api

import com.solana.models.RecentBlockhash
import java.lang.RuntimeException

fun Api.getRecentBlockhash(onComplete: ((Result<String>) -> Unit)) {
    return router.call("getRecentBlockhash", null, RecentBlockhash::class.java){ result ->
        result.onSuccess { recentBlockHash ->
            onComplete(Result.success(recentBlockHash.value.blockhash))
            return@call
        }.onFailure {
            onComplete(Result.failure(RuntimeException(it)))
            return@call
        }
    }
}