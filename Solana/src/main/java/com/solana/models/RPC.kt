package com.solana.models

import com.solana.models.buffer.*
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
open class RPC<T>(
    open var context: Context?,
    open val value: T? = null
) {
    @JsonClass(generateAdapter = true)
    class Context (val slot: Long)
}

