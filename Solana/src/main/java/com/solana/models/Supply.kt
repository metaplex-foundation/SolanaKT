package com.solana.models

import com.squareup.moshi.Json

class Supply : RpcResultObject() {
    class Value {
        @Json(name = "total")
        private val total: Long = 0

        @Json(name = "circulating")
        private val circulating: Long = 0

        @Json(name = "nonCirculating")
        private val nonCirculating: Long = 0

        @Json(name = "nonCirculatingAccounts")
        private val nonCirculatingAccounts: List<String>? = null
    }

    @Json(name = "value")
    private val value: Value? = null
}