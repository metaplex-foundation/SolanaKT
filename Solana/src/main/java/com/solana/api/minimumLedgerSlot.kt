package com.solana.api

fun Api.minimumLedgerSlot(onComplete: ((Result<Long>) -> Unit)) {
    router.call("minimumLedgerSlot", ArrayList(), Long::class.javaObjectType, onComplete)
}