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

@JsonClass(generateAdapter = true)
class RPCBuffer<T>(
    override var context: Context?,
    override val value: BufferInfo<T>? = null
) : RPC<BufferInfo<T>>(context, value)

open class BufferInfo<T>(
    var data: Buffer<T>? = null,
    val executable: Boolean,
    val lamports: Double,
    val owner: String?,
    val rentEpoch: Double
)
