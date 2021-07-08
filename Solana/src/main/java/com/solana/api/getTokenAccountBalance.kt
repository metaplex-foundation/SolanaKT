package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.RPC
import com.solana.models.TokenResultObjects
import com.squareup.moshi.Types

fun Api.getTokenAccountBalance(tokenAccount: PublicKey,
                           onComplete: (Result<TokenResultObjects.TokenAmountInfo>) -> Unit)  {
    val params: MutableList<Any> = ArrayList()
    params.add(tokenAccount.toString())
    val type = Types.newParameterizedType(
        RPC::class.java,
        TokenResultObjects.TokenAmountInfo::class.java
    )
    router.request<RPC<TokenResultObjects.TokenAmountInfo>>(
        "getTokenAccountBalance",
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