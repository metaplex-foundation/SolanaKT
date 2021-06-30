package com.solana.api

fun Api.getFirstAvailableBlock(onComplete: ((Result<Long>) -> Unit)){
    router.request("getFirstAvailableBlock", ArrayList(), Long::class.javaObjectType, onComplete)
}