package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.BufferInfo
import com.solana.models.RPC
import com.solana.vendor.borshj.BorshCodable

fun <T: BorshCodable>Api.getAccountInfo(account: PublicKey,
                                        decodeTo: Class<T>,
                                        onComplete: ((Result<BufferInfo<T>>) -> Unit)) {
    return getAccountInfo(account, HashMap(), decodeTo, onComplete)
}

fun <T: BorshCodable> Api.getAccountInfo(account: PublicKey,
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
    router.request("getAccountInfo", params, Map::class.java) { result ->
        result
            .map {
                it as Map<String, Any>
            }
            .map {
                RPC(it, decodeTo)
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