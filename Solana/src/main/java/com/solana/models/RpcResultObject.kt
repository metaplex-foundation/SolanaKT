package com.solana.models

import com.squareup.moshi.Json

open class RpcResultObject {
    class Context {
        @Json(name = "slot")
        val slot: Long = 0
    }

    @Json(name = "context")
    protected var context: Context? = null
    fun gContext(): Context? {
        return context
    }
}