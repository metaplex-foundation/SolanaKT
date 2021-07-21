package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpochInfo (
    val absoluteSlot: Long,
    val blockHeight: Long,
    val epoch: Long,
    val slotIndex: Long,
    val slotsInEpoch: Long
)