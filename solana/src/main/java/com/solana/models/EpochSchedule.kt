package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpochSchedule (
    val slotsPerEpoch: Long,
    val leaderScheduleSlotOffset: Long,
    val warmup: Boolean,
    val firstNormalEpoch: Long,
    val firstNormalSlot: Long
)