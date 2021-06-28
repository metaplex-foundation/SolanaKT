package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.SplTokenAccountInfo

fun Api.getSplTokenAccountInfo(account: PublicKey, onComplete: (Result<SplTokenAccountInfo>) -> Unit) {
    val params: MutableList<Any> = ArrayList()
    val parameterMap: MutableMap<String, Any> = HashMap()
    parameterMap["encoding"] = "jsonParsed"
    params.add(account.toString())
    params.add(parameterMap)
    router.call("getAccountInfo", params, SplTokenAccountInfo::class.java, onComplete)
}