package com.solana.api

fun Api.getBlockHeight(onComplete: ((Result<Long>) -> Unit)) {
    router.request("getBlockHeight", ArrayList(), Long::class.javaObjectType, onComplete)
}