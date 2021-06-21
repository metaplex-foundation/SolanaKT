package com.solana.networking.models

import com.solana.models.RpcResultObject
import com.squareup.moshi.Json

class RpcResultTypes {
    class ValueLong : RpcResultObject() {
        @Json(name = "value")
        val value: Long = 0
    }
}