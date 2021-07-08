package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BlockCommitment(
    var commitment: LongArray?,
    var totalStake: Long
)