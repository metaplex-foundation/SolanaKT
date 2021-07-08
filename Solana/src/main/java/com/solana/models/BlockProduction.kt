package com.solana.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BlockProduction(val value: BlockProductionValue) {
    class BlockProductionRange (
        val firstSlot: Long,
        val lastSlot: Long
    )
    class BlockProductionValue (
        val byIdentity: Map<String, List<Double>>?,
        val blockProductionRange: BlockProductionRange?
    )
}