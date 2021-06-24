package com.solana.models

import com.squareup.moshi.Json
import java.util.*

class SignatureInformation(info: Map<String, Any>) {
    @Json(name = "err")
    private var err: Any? = info["err"]

    @Json(name = "memo")
    private val memo: Any? = info["memo"]

    @Json(name = "signature")
    private val signature: String? = info["signature"] as String?

    @Json(name = "slot")
    private val slot: Long? = (info["slot"] as Double).toLong()
}