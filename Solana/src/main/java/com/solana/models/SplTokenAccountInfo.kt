package com.solana.models

import com.solana.models.buffer.BufferInfo
import com.squareup.moshi.JsonClass

const val REQUIRED_ACCOUNT_SPACE = 165L
@JsonClass(generateAdapter = true)
data class SplTokenAccountInfo(
    override val value: BufferInfo<TokenResultObjects.Data>
) : RPC<BufferInfo<TokenResultObjects.Data>>(null, value)