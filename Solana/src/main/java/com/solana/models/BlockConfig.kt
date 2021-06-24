package com.solana.models

import com.squareup.moshi.Json

class BlockConfig (
    @Json(name = "encoding")
    val encoding: String = "json",

    @Json(name = "transactionDetails")
    val transactionDetails: String = "full",

    @Json(name = "rewards")
    val rewards: Boolean = true,

    @Json(name = "commitment")
    val commitment: String = "finalized"
)