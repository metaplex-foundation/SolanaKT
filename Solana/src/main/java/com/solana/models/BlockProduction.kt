package com.solana.models

import com.squareup.moshi.Json

class BlockProduction(@Json(name = "value") val value: BlockProductionValue) {
    class BlockProductionRange (
        @Json(name = "firstSlot")
        val firstSlot: Long,

        @Json(name = "lastSlot")
        val lastSlot: Long
    )

    class BlockProductionValue (
        @Json(name = "byIdentity")
        val byIdentity: Map<String, List<Double>>?,
        @Json(name = "range")
        private val blockProductionRange: BlockProductionRange?
    )
}