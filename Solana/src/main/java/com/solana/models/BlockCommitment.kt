package com.solana.models

import com.squareup.moshi.Json

class BlockCommitment(
    @Json(name = "commitment")
    var commitment: LongArray?,
    @Json(name = "totalStake")
    var totalStake: Long
)