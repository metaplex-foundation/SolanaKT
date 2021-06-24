package com.solana.models

import com.squareup.moshi.Json

class RpcEpochConfig(
    @Json(name = "epoch") val epoch: Long,
    @Json(name = "commitment") val commitment: String?
)