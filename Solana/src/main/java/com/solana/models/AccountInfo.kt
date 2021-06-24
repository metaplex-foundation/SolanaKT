package com.solana.models

import com.squareup.moshi.Json

class AccountInfo(@Json(name = "value") private val value: Value) : RpcResultObject() {
    class Value (
        @Json(name = "data")
        private val data: List<String>,

        @Json(name = "executable")
        private val executable:Boolean,

        @Json(name = "lamports")
        private val lamports: Long,

        @Json(name = "owner")
        private val owner: String?,

        @Json(name = "rentEpoch")
        private val rentEpoch: Long,
    )
}