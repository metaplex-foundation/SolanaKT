package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.RPC
import com.solana.models.TokenResultObjects
import com.squareup.moshi.Types

fun Api.getTokenLargestAccounts(tokenMint: PublicKey,
                            onComplete: (Result<List<TokenResultObjects.TokenAccount>>) -> Unit) {
    val params: MutableList<Any> = ArrayList()
    params.add(tokenMint.toString())
    val type = Types.newParameterizedType(
        RPC::class.java,
        Types.newParameterizedType(
            List::class.java,
            TokenResultObjects.TokenAccount::class.java
        )
    )
    router.request<RPC<List<TokenResultObjects.TokenAccount>>>(
        "getTokenLargestAccounts", params,
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