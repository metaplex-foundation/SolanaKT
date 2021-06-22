package com.solana.models

import com.squareup.moshi.Json
import java.util.*

class SignatureInformation(info: AbstractMap<*, *>) {
    @Json(name = "err")
    private var err: Any?

    @Json(name = "memo")
    private val memo: Any?

    @Json(name = "signature")
    private val signature: String?

    @Json(name = "slot")
    private val slot: Long = 0

    init {
        err = info["err"]
        memo = info["memo"]
        signature = info["signature"] as String?
        err = info["slot"]
    }
}