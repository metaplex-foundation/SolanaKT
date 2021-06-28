package com.solana.api

fun Api.getMaxShredInsertSlot(onComplete: (Result<Long>) -> Unit) {
    router.call("getMaxShredInsertSlot", ArrayList(), Long::class.javaObjectType, onComplete)
}