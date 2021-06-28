package com.solana.api

fun Api.getMaxRetransmitSlot(onComplete: ((Result<Long>) -> Unit)) {
    router.call("getMaxRetransmitSlot", ArrayList(), Long::class.javaObjectType, onComplete)
}
