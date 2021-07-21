package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.TokenAccountInfo

fun Api.getTokenAccount(
    account: PublicKey,
    requiredParams: Map<String, Any>,
    optionalParams: Map<String, Any>?,
    method: String,
    onComplete: (Result<TokenAccountInfo>) -> Unit
) {
    val params: MutableList<Any> = ArrayList()
    params.add(account.toString())

    // Either mint or programId is required
    var parameterMap: MutableMap<String?, Any?> = HashMap()
    if (requiredParams.containsKey("mint")) {
        parameterMap["mint"] = requiredParams["mint"].toString()
    } else if (requiredParams.containsKey("programId")) {
        parameterMap["programId"] = requiredParams["programId"].toString()
    } else {
        onComplete(Result.failure(ApiError("mint or programId are mandatory parameters")))
        return
    }
    params.add(parameterMap)
    if (null != optionalParams) {
        parameterMap = HashMap()
        parameterMap["commitment"] = optionalParams["commitment"] ?: "max"
        parameterMap["encoding"] = optionalParams["encoding"] ?: "jsonParsed"
        // No default for dataSlice
        if (optionalParams.containsKey("dataSlice")) {
            parameterMap["dataSlice"] = optionalParams["dataSlice"]
        }
        params.add(parameterMap)
    }
    router.request(method, params, TokenAccountInfo::class.java, onComplete)
}