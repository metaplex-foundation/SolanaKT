package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.RPC
import com.solana.models.TokenAccountInfo
import com.squareup.moshi.Types

fun Api.getTokenAccountsByOwner(owner: PublicKey, tokenMint: PublicKey, onComplete: (Result<PublicKey>) -> Unit) {
    val params: MutableList<Any> = ArrayList()
    params.add(owner.toBase58())
    val parameterMap: MutableMap<String, Any> = HashMap()
    parameterMap["mint"] = tokenMint.toBase58()
    params.add(parameterMap)

    val encodingMap: MutableMap<String, Any> = HashMap()
    encodingMap["encoding"] = "base64"
    params.add(encodingMap)

    val type = Types.newParameterizedType(
        RPC::class.java,
        Types.newParameterizedType(
            List::class.java,
            Types.newParameterizedType(
                Map::class.java,
                String::class.java,
                Any::class.java
            )
        )
    )

    router.request<RPC<List<Map<String,Any>>>>(
        "getTokenAccountsByOwner", params,
        type
    ) { result ->
        result.map {
            it.value!!.first().get("pubkey") as String
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