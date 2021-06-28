package com.solana.api

fun Api.getBlockHeight(onComplete: ((Result<Long>) -> Unit)) {
    router.call("getBlockHeight", ArrayList(), Long::class.javaObjectType, onComplete)
}