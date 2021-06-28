package com.solana.api

import com.solana.models.BlockCommitment

fun Api.getBlockCommitment(block: Long, onComplete: ((Result<BlockCommitment>) -> Unit)) {
    val params: MutableList<Any> = ArrayList()
    params.add(block)
    router.call("getBlockCommitment", params, BlockCommitment::class.java, onComplete)
}