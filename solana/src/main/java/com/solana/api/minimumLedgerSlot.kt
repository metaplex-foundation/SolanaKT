package com.solana.api

fun Api.minimumLedgerSlot(onComplete: ((Result<Long>) -> Unit)) {
    router.request("minimumLedgerSlot", ArrayList(), Long::class.javaObjectType, onComplete)
}