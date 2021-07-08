package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Reward (
    val pubkey: String?,
    val lamports:Long,
    val postBalance: String?,
    val rewardType: RewardType?
)