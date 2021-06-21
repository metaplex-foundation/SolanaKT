package com.solana.models

import com.squareup.moshi.Json

class RecentBlockhash : RpcResultObject() {
    class FeeCalculator {
        @Json(name = "lamportsPerSignature")
        val lamportsPerSignature: Long = 0
    }

    class Value {
        @Json(name = "blockhash")
        val blockhash: String? = null

        @Json(name = "feeCalculator")
        val feeCalculator: FeeCalculator? = null
    }

    @Json(name = "value")
    val value: Value? = null
    val recentBlockhash: String?
        get() = value!!.blockhash
}