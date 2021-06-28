package com.solana.api

import com.solana.core.PublicKey
import com.solana.networking.models.RpcResultTypes

fun Api.getBalance(account: PublicKey, onComplete: ((Result<Long>) -> Unit)) {
    val params: MutableList<Any> = ArrayList()
    params.add(account.toString())
    return router.call("getBalance", params, RpcResultTypes.ValueLong::class.java){ result ->
        result.onSuccess {
            onComplete(Result.success(it.value))
            return@call
        }.onFailure {
            onComplete(Result.failure(RuntimeException(it)))
            return@call
        }
    }
}