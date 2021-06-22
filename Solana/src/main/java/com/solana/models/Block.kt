package com.solana.models

import com.squareup.moshi.Json

data class Block (
    @Json(name = "blockTime") val blockTime: Long,
    @Json(name = "blockHeight") val blockHeight: String?,
    @Json(name = "blockhash") val blockhash: String?,
    @Json(name = "parentSlot") val parentSlot: Long,
    @Json(name = "previousBlockhash") val previousBlockhash: String?,
    @Json(name = "transactions") val transactions: List<ConfirmedTransaction>? ,
    @Json(name = "rewards") val rewards: List<Reward>?,
)