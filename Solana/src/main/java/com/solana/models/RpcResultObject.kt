package com.solana.models

import com.solana.models.Buffer.*
import com.squareup.moshi.JsonClass

typealias RpcResultObject<T> = RPC2<T>

@JsonClass(generateAdapter = true)
open class RPC2<T>(
    var context: Context?,
    open val value: T? = null
) {
    @JsonClass(generateAdapter = true)
    class Context (val slot: Long)
}

@JsonClass(generateAdapter = true)
open class RPC3<T>(
    var context: Context?,
    open val value: BufferInfo2<T>? = null
) {
    @JsonClass(generateAdapter = true)
    class Context (val slot: Long)
}

@JsonClass(generateAdapter = true)
open class BufferInfo2<T>(
    var data: Buffer2<T>? = null,
    val executable: Boolean,
    val lamports: Double,
    val owner: String?,
    val rentEpoch: Double
)
