package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignatureInformation(
    private var err: Any?,
    private val memo: Any?,
    private val signature: String?,
    private val slot: Long?
)