package com.solana.api

fun Api.getFirstAvailableBlock(onComplete: ((Result<Long>) -> Unit)){
    router.call("getFirstAvailableBlock", ArrayList(), Long::class.javaObjectType, onComplete)
}