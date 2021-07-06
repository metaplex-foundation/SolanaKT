package com.solana.networking.models

import com.solana.models.RpcResultObject
import com.squareup.moshi.Json

class RpcResultTypes {
    class ValueLong(@Json(name = "value") override val value: Long) : RpcResultObject<Long>(null, value)
}