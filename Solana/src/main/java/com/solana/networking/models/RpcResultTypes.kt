package com.solana.networking.models

import com.solana.models.RpcResultObject
import com.squareup.moshi.Json

class RpcResultTypes {
    class ValueLong(@Json(name = "value") val value: Long) : RpcResultObject()
}