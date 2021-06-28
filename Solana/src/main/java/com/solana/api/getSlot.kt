package com.solana.api

fun Api.getSlot(onComplete: (Result<Long>) -> Unit) {
    router.call("getSlot", ArrayList(), Long::class.javaObjectType, onComplete)
}