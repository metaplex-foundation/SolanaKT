package com.solana.models

import com.squareup.moshi.Json

class EpochInfo (
    @Json(name = "absoluteSlot")
    private val absoluteSlot: Long,

    @Json(name = "blockHeight")
    private val blockHeight: Long,

    @Json(name = "epoch")
    private val epoch: Long,

    @Json(name = "slotIndex")
    private val slotIndex: Long,

    @Json(name = "slotsInEpoch")
    private val slotsInEpoch: Long
)