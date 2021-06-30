package com.solana.api

fun Api.getBlockTime(block: Long, onComplete: ((Result<Long>) -> Unit)) {
    val params: MutableList<Any> = ArrayList()
    params.add(block)
    router.request("getBlockTime", params, Long::class.javaObjectType, onComplete)
}