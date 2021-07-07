package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.BufferInfo
import com.solana.models.RPCBuffer

fun <T>Api.getAccountInfo(account: PublicKey,
                                        decodeTo: Class<T>,
                                        onComplete: ((Result<BufferInfo<T>>) -> Unit)) {
    return getAccountInfo(account, HashMap(), decodeTo, onComplete)
}

fun <T> Api.getAccountInfo(account: PublicKey,
                          additionalParams: Map<String, Any?>,
                          decodeTo: Class<T>,
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

    router.request("getAccountInfo", params, decodeTo) { result ->
        result
            .map {
                it as RPCBuffer<T>
            }
            .map {
                it.value as BufferInfo
            }
            .onSuccess {
                onComplete(Result.success(it))
            }
            .onFailure {
                onComplete(Result.failure(it))
            }
    }
}

