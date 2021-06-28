package com.solana.api

fun Api.getTransactionCount(onComplete: ((Result<Long>) -> Unit)) {
    router.call("getTransactionCount", ArrayList(), Long::class.javaObjectType, onComplete)
}