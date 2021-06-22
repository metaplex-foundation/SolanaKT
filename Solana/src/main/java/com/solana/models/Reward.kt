package com.solana.models

import com.squareup.moshi.Json

class Reward {
    @Json(name = "pubkey")
    private val pubkey: String? = null

    @Json(name = "lamports")
    private val lamports = 0

    @Json(name = "postBalance")
    private val postBalance: String? = null

    @Json(name = "rewardType")
    private val rewardType: RewardType? = null
}