package com.solana.models

import com.squareup.moshi.Json

class SimulatedTransaction : RpcResultObject() {
    class Value {
        @Json(name = "accounts")
        private val accounts: List<AccountInfo.Value>? = null

        @Json(name = "logs")
        private val logs: List<String>? = null
    }

    @Json(name = "value")
    private val value: Value? = null
}