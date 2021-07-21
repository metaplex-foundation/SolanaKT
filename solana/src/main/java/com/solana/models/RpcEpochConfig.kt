package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RpcEpochConfig(
    val epoch: Long,
    val commitment: String?
)