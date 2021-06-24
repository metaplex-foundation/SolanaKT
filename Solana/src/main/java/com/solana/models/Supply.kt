package com.solana.models

import com.squareup.moshi.Json

class Supply(@Json(name = "value") val value: Value) : RpcResultObject() {
    class Value (
        @Json(name = "total")
        private val total: Long,

        @Json(name = "circulating")
        private val circulating: Long,

        @Json(name = "nonCirculating")
        private val nonCirculating: Long,

        @Json(name = "nonCirculatingAccounts")
        private val nonCirculatingAccounts: List<String>
    )
}