package com.solana.networking.models

import com.solana.models.RPC
import com.squareup.moshi.Json

class RpcResultTypes {
    class ValueLong(@Json(name = "value") override val value: Long) : RPC<Long>(null, value)
}