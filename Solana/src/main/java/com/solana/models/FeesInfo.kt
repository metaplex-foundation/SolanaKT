package com.solana.models

import com.solana.models.RecentBlockhash.FeeCalculator
import com.squareup.moshi.Json

class FeesInfo : RpcResultObject() {
    class Value {
        @Json(name = "blockhash")
        private val blockhash: String? = null

        @Json(name = "feeCalculator")
        private val feeCalculator: FeeCalculator? = null

        @Json(name = "lastValidSlot")
        private val lastValidSlot: Long = 0

        @Json(name = "lastValidBlockHeight")
        private val lastValidBlockHeight: Long = 0
    }

    @Json(name = "value")
    private val value: Value? = null
}