package com.solana.models

import com.squareup.moshi.Json

class StakeActivationConfig (
    @Json(name = "epoch") val epoch: Long
)