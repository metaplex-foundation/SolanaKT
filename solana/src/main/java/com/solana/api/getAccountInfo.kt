package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.RPC
import com.solana.models.buffer.BufferInfo
import com.squareup.moshi.Types
import java.lang.reflect.Type
import com.solana.vendor.ResultError

fun <T>Api.getAccountInfo(account: PublicKey,
                                        decodeTo: Class<T>,
                                        onComplete: ((com.solana.vendor.Result<BufferInfo<T>, ResultError>) -> Unit)) {
    return getAccountInfo(account, HashMap(), decodeTo, onComplete)
}

fun <T> Api.getAccountInfo(account: PublicKey,
                          additionalParams: Map<String, Any?>,
                          decodeTo: Class<T>,
                          onComplete: ((com.solana.vendor.Result<BufferInfo<T>, ResultError>) -> Unit)) {


    val params: MutableList<Any> = ArrayList()
    val parameterMap: MutableMap<String, Any?> = HashMap()
    parameterMap["commitment"] = additionalParams.getOrDefault("commitment", "max")
    parameterMap["encoding"] = additionalParams.getOrDefault("encoding", "base64")

    if (additionalParams.containsKey("dataSlice")) {
        parameterMap["dataSlice"] = additionalParams["dataSlice"]
    }
    params.add(account.toString())
    params.add(parameterMap)

    val type = Types.newParameterizedType(
        RPC::class.java,
        Types.newParameterizedType(
            BufferInfo::class.java,
            Type::class.java.cast(decodeTo)
        )
    )

    router.request<RPC<BufferInfo<T>?>>("getAccountInfo", params, type) { result ->
        result
            .map {
                it.value
            }
            .onSuccess {
                if(it != null){
                    onComplete(com.solana.vendor.Result.success(it))
                } else {
                    onComplete(com.solana.vendor.Result.failure(nullValueError))
                }
            }
            .onFailure {
                onComplete(com.solana.vendor.Result.failure(ResultError(it)))
            }
    }
}

val nullValueError = ResultError("Account return Null")

