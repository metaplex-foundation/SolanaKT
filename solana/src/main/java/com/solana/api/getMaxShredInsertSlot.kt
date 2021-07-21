package com.solana.api

fun Api.getMaxShredInsertSlot(onComplete: (Result<Long>) -> Unit) {
    router.request("getMaxShredInsertSlot", ArrayList(), Long::class.javaObjectType, onComplete)
}