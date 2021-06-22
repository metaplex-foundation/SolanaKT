package com.solana.models

import com.squareup.moshi.Json

class EpochInfo {
    @Json(name = "absoluteSlot")
    private val absoluteSlot: Long = 0

    @Json(name = "blockHeight")
    private val blockHeight: Long = 0

    @Json(name = "epoch")
    private val epoch: Long = 0

    @Json(name = "slotIndex")
    private val slotIndex: Long = 0

    @Json(name = "slotsInEpoch")
    private val slotsInEpoch: Long = 0
}