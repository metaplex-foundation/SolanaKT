package com.solana.api

fun Api.getGenesisHash(onComplete: ((Result<String>) -> Unit)){
    router.request("getGenesisHash", ArrayList(), String::class.java, onComplete)
}