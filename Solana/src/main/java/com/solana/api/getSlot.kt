package com.solana.api

fun Api.getSlot(onComplete: (Result<Long>) -> Unit) {
    router.request("getSlot", ArrayList(), Long::class.javaObjectType, onComplete)
}