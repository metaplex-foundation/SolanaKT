package com.solana.models

import com.squareup.moshi.Json

class StakeActivationConfig {
    @Json(name = "epoch")
    private val epoch: Long = 0
}