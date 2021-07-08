package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConfirmedBlock (
    val blockTime: Long,
    val blockhash: String?,
    val parentSlot:Long,
    val previousBlockhash: String?,
    val transactions: List<ConfirmedTransaction>?
)