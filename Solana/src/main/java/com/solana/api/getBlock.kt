package com.solana.api

import com.solana.models.Block
import com.solana.models.BlockConfig

fun Api.getBlock(slot: Int, onComplete: ((Result<Block>) -> Unit)) {
    val params: MutableList<Any> = ArrayList()
    params.add(slot)
    params.add(BlockConfig())
    router.call("getBlock", params, Block::class.java, onComplete)
}