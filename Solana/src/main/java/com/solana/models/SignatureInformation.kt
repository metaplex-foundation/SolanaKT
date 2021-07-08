package com.solana.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
class SignatureInformation(
    private var err: Any?,
    private val memo: Any?,
    private val signature: String?,
    private val slot: Long?
)