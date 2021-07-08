package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.RPC
import com.solana.models.TokenResultObjects
import com.solana.models.buffer.BufferInfo
import com.squareup.moshi.Types
import java.lang.reflect.Type

fun Api.getTokenSupply(tokenMint: PublicKey,
                   onComplete: (Result<TokenResultObjects.TokenAmountInfo>) -> Unit) {
    val params: MutableList<Any> = ArrayList()
    params.add(tokenMint.toString())
    val type = Types.newParameterizedType(
        RPC::class.java,
        TokenResultObjects.TokenAmountInfo::class.java
    )
    router.request<RPC<TokenResultObjects.TokenAmountInfo>>(
        "getTokenSupply",
        params,
        type
    ){ result ->
        result.map {
            it.value!!
        }.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}