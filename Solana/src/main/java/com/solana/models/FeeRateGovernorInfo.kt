package com.solana.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeeRateGovernorInfo(
    override val value: Value
) : RPC<FeeRateGovernorInfo.Value>(null, value) {
    @JsonClass(generateAdapter = true)
    data class FeeRateGovernor (
        val burnPercent: Long,
        val maxLamportsPerSignature: Long,
        val minLamportsPerSignature: Long,
        val targetLamportsPerSignature: Long,
        val targetSignaturesPerSlot: Long
    )

    @JsonClass(generateAdapter = true)
    data class Value (
        val feeRateGovernor: FeeRateGovernor
    )
}