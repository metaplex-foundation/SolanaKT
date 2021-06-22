package com.solana.models

import com.squareup.moshi.Json

class FeeRateGovernorInfo : RpcResultObject() {
    class FeeRateGovernor {
        @Json(name = "burnPercent")
        private val burnPercent = 0

        @Json(name = "maxLamportsPerSignature")
        private val maxLamportsPerSignature: Long = 0

        @Json(name = "minLamportsPerSignature")
        private val minLamportsPerSignature: Long = 0

        @Json(name = "targetLamportsPerSignature")
        private val targetLamportsPerSignature: Long = 0

        @Json(name = "targetSignaturesPerSlot")
        private val targetSignaturesPerSlot: Long = 0
    }

    class Value {
        @Json(name = "feeRateGovernor")
        private val feeRateGovernor: FeeRateGovernor? = null
    }

    @Json(name = "value")
    private val value: Value? = null
}