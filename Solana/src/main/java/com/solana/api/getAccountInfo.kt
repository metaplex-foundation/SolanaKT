package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.Buffer.BufferLayout
import com.solana.models.BufferInfo
import com.solana.models.RPC

fun <T>Api.getAccountInfo(account: PublicKey,
                          decodeTo: Class<T>,
                          bufferLayout: BufferLayout,
                          onComplete: ((Result<BufferInfo<T>>) -> Unit)) {
    return getAccountInfo(account, HashMap(), decodeTo, bufferLayout, onComplete)
}

fun <T>Api.getAccountInfo(account: PublicKey,
                          additionalParams: Map<String, Any?>,
                          decodeTo: Class<T>,
                          bufferLayout: BufferLayout,
                          onComplete: ((Result<BufferInfo<T>>) -> Unit)) {
    val params: MutableList<Any> = ArrayList()
    val parameterMap: MutableMap<String, Any?> = HashMap()
    parameterMap["commitment"] = additionalParams.getOrDefault("commitment", "max")
    parameterMap["encoding"] = additionalParams.getOrDefault("encoding", "base64")

    if (additionalParams.containsKey("dataSlice")) {
        parameterMap["dataSlice"] = additionalParams["dataSlice"]
    }
    params.add(account.toString())
    params.add(parameterMap)
    router.call("getAccountInfo", params, Map::class.java) { result ->
        result
            .map {
                it as Map<String, Any>
            }
            .map {
                RPC(it, decodeTo, bufferLayout)
            }
            .map {
                it.value as BufferInfo<T>
            }
            .onSuccess {
                onComplete(Result.success(it))
            }
            .onFailure {
                onComplete(Result.failure(it))
            }
    }
}