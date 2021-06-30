package com.solana.models

import com.squareup.moshi.Json
import java.io.ByteArrayInputStream
import java.io.ObjectInput
import java.io.ObjectInputStream

class AccountInfo(
    @Json(name = "value") val value: Value
) : RpcResultObject() {
    class Value (
        @Json(name = "data")
        val data: List<String>,

        @Json(name = "executable")
        val executable:Boolean,

        @Json(name = "lamports")
        val lamports: Long,

        @Json(name = "owner")
        val owner: String?,

        @Json(name = "rentEpoch")
        val rentEpoch: Long,
    )
}
