package com.solana.models.buffer

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class BufferInfo<T>(
    var data: Buffer<T>? = null,
    val executable: Boolean,
    val lamports: Double,
    val owner: String?,
    val rentEpoch: Double
)