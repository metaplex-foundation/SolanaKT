package com.solana.models

import com.squareup.moshi.Json

class RecentBlockhash(
    @Json(name = "value") val value: Value
) : RpcResultObject() {
    class FeeCalculator (
        @Json(name = "lamportsPerSignature")
        val lamportsPerSignature: Long = 0
    )

    class Value (
        @Json(name = "blockhash")
        val blockhash: String,
        @Json(name = "feeCalculator")
        val feeCalculator: FeeCalculator
    )

}