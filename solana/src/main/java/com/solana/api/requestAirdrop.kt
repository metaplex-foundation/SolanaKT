package com.solana.api

import com.solana.core.PublicKey

fun Api.requestAirdrop(address: PublicKey, lamports: Long, onComplete: ((Result<String>) -> Unit)) {
    val params: MutableList<Any> = ArrayList()
    params.add(address.toString())
    params.add(lamports)
    router.request("requestAirdrop", params, String::class.java, onComplete)
}