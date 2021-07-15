package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.RPC
import com.solana.models.buffer.BufferInfo
import com.squareup.moshi.Types
import java.lang.reflect.Type

fun <T> Api.getMultipleAccounts(
    accounts: List<PublicKey>,
    decodeTo: Class<T>,
    onComplete: ((Result<List<BufferInfo<T>>>) -> Unit)
) {

    val publickeys = accounts.map { it.toBase58() }
    val params: MutableList<Any> = ArrayList()
    val parameterMap: MutableMap<String, Any?> = HashMap()

    parameterMap["encoding"] = "base64"
    params.add(publickeys)
    params.add(parameterMap)

    val type = Types.newParameterizedType(
        RPC::class.java,
        Types.newParameterizedType(
            List::class.java,
            Types.newParameterizedType(
                BufferInfo::class.java,
                Type::class.java.cast(decodeTo)
            )
        )
    )

    router.request<RPC<List<BufferInfo<T>>>>("getMultipleAccounts", params, type) { result ->
        result
            .map {
                it.value!!
            }
            .onSuccess {
                onComplete(Result.success(it))
            }
            .onFailure {
                onComplete(Result.failure(it))
            }
    }
}