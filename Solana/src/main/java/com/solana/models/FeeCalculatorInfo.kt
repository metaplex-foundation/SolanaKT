package com.solana.models

import com.solana.models.RecentBlockhash.FeeCalculator
import com.squareup.moshi.Json

class FeeCalculatorInfo : RpcResultObject() {
    class Value {
        @Json(name = "feeCalculator")
        private val feeCalculator: FeeCalculator? = null
    }

    @Json(name = "value")
    private val value: Value? = null
}