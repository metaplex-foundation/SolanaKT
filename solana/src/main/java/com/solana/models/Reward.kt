package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Reward (
    val pubkey: String? = null,
    val lamports: Long = 0,
    val postBalance: String? = null,
    val rewardType: RewardType? = null,
)