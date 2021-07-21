package com.solana.api

fun Api.getMaxRetransmitSlot(onComplete: ((Result<Long>) -> Unit)) {
    router.request("getMaxRetransmitSlot", ArrayList(), Long::class.javaObjectType, onComplete)
}
