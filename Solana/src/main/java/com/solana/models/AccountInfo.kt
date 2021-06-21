package com.solana.models

import com.squareup.moshi.Json

class AccountInfo : RpcResultObject() {
    class Value {
        @Json(name = "data")
        val data: List<String>? = null

        @Json(name = "executable")
        val isExecutable = false

        @Json(name = "lamports")
        val lamports: Long = 0

        @Json(name = "owner")
        val owner: String? = null

        @Json(name = "rentEpoch")
        val rentEpoch: Long = 0
    }

    @Json(name = "value")
    val value: Value? = null
}