package com.solana.api

import com.solana.models.BlockConfig
import com.solana.models.ConfirmedBlock

fun Api.getConfirmedBlock(slot: Int, onComplete: (Result<ConfirmedBlock>) -> Unit) {
    val params: MutableList<Any> = ArrayList()
    params.add(slot)
    params.add(BlockConfig())
    router.call("getConfirmedBlock", params, ConfirmedBlock::class.java, onComplete)
}