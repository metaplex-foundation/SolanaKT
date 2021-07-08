package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BlockProduction(
    val value: BlockProductionValue
) {
    @JsonClass(generateAdapter = true)
    data class BlockProductionRange (
        val firstSlot: Long,
        val lastSlot: Long
    )

    @JsonClass(generateAdapter = true)
    data class BlockProductionValue (
        val byIdentity: Map<String, List<Double>>?,
        val blockProductionRange: BlockProductionRange?
    )
}