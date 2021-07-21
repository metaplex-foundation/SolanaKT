package com.solana.api

fun Api.getTransactionCount(onComplete: ((Result<Long>) -> Unit)) {
    router.request("getTransactionCount", ArrayList(), Long::class.javaObjectType, onComplete)
}