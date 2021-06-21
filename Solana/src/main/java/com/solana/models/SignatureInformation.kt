package com.solana.models

import com.squareup.moshi.Json
import java.util.*

class SignatureInformation {
    @Json(name = "err")
    var err: Any? = null
        private set

    @Json(name = "memo")
    var memo: Any? = null
        private set

    @Json(name = "signature")
    var signature: String? = null
        private set

    @Json(name = "slot")
    val slot: Long = 0

    constructor() {}
    constructor(info: Map<String, Any>) {
        err = info["err"]
        memo = info["memo"]
        signature = info["signature"] as String?
        err = info["slot"]
    }
}