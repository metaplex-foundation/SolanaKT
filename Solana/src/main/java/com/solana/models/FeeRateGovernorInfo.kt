package com.solana.models

import com.squareup.moshi.Json

class FeeRateGovernorInfo(@Json(name = "value") val value: Value) : RpcResultObject() {
    class FeeRateGovernor (
        @Json(name = "burnPercent")
        val burnPercent: Long,

        @Json(name = "maxLamportsPerSignature")
        val maxLamportsPerSignature: Long,

        @Json(name = "minLamportsPerSignature")
        val minLamportsPerSignature: Long,

        @Json(name = "targetLamportsPerSignature")
        val targetLamportsPerSignature: Long,

        @Json(name = "targetSignaturesPerSlot")
        val targetSignaturesPerSlot: Long
    )

    class Value (
        @Json(name = "feeRateGovernor") val feeRateGovernor: FeeRateGovernor
    )
}