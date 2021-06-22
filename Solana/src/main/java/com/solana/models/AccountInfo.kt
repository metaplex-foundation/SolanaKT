package com.solana.models

import com.squareup.moshi.Json

class AccountInfo(@Json(name = "value") private val value: Value? = null) : RpcResultObject() {
    class Value {
        @Json(name = "data")
        private val data: List<String>? = null

        @Json(name = "executable")
        private val executable = false

        @Json(name = "lamports")
        private val lamports: Long = 0

        @Json(name = "owner")
        private val owner: String? = null

        @Json(name = "rentEpoch")
        private val rentEpoch: Long = 0
    }
}