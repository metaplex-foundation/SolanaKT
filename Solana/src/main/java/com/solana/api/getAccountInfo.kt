package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.AccountInfo

fun Api.getAccountInfo(account: PublicKey, onComplete: ((Result<AccountInfo>) -> Unit)) {
    return getAccountInfo(account, HashMap(), onComplete)
}

fun Api.getAccountInfo(account: PublicKey, additionalParams: Map<String, Any?>, onComplete: ((Result<AccountInfo>) -> Unit)) {
    val params: MutableList<Any> = ArrayList()
    val parameterMap: MutableMap<String, Any?> = HashMap()
    parameterMap["commitment"] = additionalParams.getOrDefault("commitment", "max")
    parameterMap["encoding"] = additionalParams.getOrDefault("encoding", "base64")

    if (additionalParams.containsKey("dataSlice")) {
        parameterMap["dataSlice"] = additionalParams["dataSlice"]
    }
    params.add(account.toString())
    params.add(parameterMap)
    router.call("getAccountInfo", params, AccountInfo::class.java, onComplete)
}