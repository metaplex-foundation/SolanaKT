package com.solana.models

import com.squareup.moshi.Json

class ConfirmedBlock (
    @Json(name = "blockTime")
    val blockTime: Long,

    @Json(name = "blockhash")
    val blockhash: String?,

    @Json(name = "parentSlot")
    val parentSlot:Long,

    @Json(name = "previousBlockhash")
    val previousBlockhash: String?,

    @Json(name = "transactions")
    val transactions: List<ConfirmedTransaction>?
)