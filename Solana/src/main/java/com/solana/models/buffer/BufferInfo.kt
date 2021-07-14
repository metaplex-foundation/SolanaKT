package com.solana.models.buffer

import com.solana.core.PublicKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class BufferInfo<T>(
    var data: Buffer<T>? = null,
    val executable: Boolean,
    val lamports: Long,
    val owner: String?,
    val rentEpoch: Long
)

@JsonClass(generateAdapter = true)
data class BufferInfoJson<T>(
    val data: T?,
    val lamports: Long,
    val owner: PublicKey,
    val executable: Boolean,
    val rentEpoch: Long
)