package com.solana.api

fun Api.getGenesisHash(onComplete: ((Result<String>) -> Unit)){
    router.call("getGenesisHash", ArrayList(), String::class.java, onComplete)
}