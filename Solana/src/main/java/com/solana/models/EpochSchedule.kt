package com.solana.models

import com.squareup.moshi.Json

class EpochSchedule {
    @Json(name = "slotsPerEpoch")
    private val slotsPerEpoch: Long = 0

    @Json(name = "leaderScheduleSlotOffset")
    private val leaderScheduleSlotOffset: Long = 0

    @Json(name = "warmup")
    private val warmup = false

    @Json(name = "firstNormalEpoch")
    private val firstNormalEpoch: Long = 0

    @Json(name = "firstNormalSlot")
    private val firstNormalSlot: Long = 0
}