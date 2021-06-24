package com.solana.models

import com.squareup.moshi.Json

class EpochSchedule (
    @Json(name = "slotsPerEpoch")
    val slotsPerEpoch: Long,

    @Json(name = "leaderScheduleSlotOffset")
    val leaderScheduleSlotOffset: Long,

    @Json(name = "warmup")
    val warmup: Boolean,

    @Json(name = "firstNormalEpoch")
    val firstNormalEpoch: Long,

    @Json(name = "firstNormalSlot")
    val firstNormalSlot: Long
)