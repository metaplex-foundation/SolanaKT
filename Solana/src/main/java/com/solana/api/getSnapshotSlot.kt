package com.solana.api

fun Api.getSnapshotSlot(onComplete: (Result<Long>) -> Unit) {
    router.call("getSnapshotSlot", ArrayList(), Long::class.javaObjectType, onComplete)
}