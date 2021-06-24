package com.solana.models

import com.squareup.moshi.Json

class SimulatedTransaction(@Json(name = "value") val value: Value) : RpcResultObject() {
    class Value (
        @Json(name = "accounts")
        private val accounts: List<AccountInfo.Value>?,

        @Json(name = "logs")
        private val logs: List<String>?,
    )
}