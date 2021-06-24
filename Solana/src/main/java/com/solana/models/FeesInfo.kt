package com.solana.models

import com.solana.models.RecentBlockhash.FeeCalculator
import com.squareup.moshi.Json


class FeesInfo(@Json(name = "value") val value: Value) : RpcResultObject(){
    class Value (
        @Json(name = "blockhash")
        val blockhash: String,

        @Json(name = "feeCalculator")
        val feeCalculator: FeeCalculator,

        @Json(name = "lastValidSlot")
        val lastValidSlot: Long,

        @Json(name = "lastValidBlockHeight")
        val lastValidBlockHeight: Long
    )
}