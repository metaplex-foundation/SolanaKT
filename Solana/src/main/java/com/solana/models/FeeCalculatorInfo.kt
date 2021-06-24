package com.solana.models

import com.solana.models.RecentBlockhash.FeeCalculator
import com.squareup.moshi.Json


class FeeCalculatorInfo(@Json(name = "value") val value: Value?) : RpcResultObject(){
    class Value (
        @Json(name = "feeCalculator") val feeCalculator: FeeCalculator
    )
}