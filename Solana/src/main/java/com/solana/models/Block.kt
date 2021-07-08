package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Block (
    val blockTime: Long,
    val blockHeight: String?,
    val blockhash: String?,
    val parentSlot: Long,
    val previousBlockhash: String?,
    val transactions: List<ConfirmedTransaction>? ,
    val rewards: List<Reward>?,
)