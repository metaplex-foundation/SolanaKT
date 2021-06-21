package com.solana.networking.models

import com.squareup.moshi.Json

class RpcResponse<T> {
    class Error {
        @Json(name = "code")
        val code: Long = 0

        @Json(name = "message")
        val message: String? = null
    }

    @Json(name = "jsonrpc")
    private val jsonrpc: String? = null

    @Json(name = "result")
    var result: T? = null
        private set

    @Json(name = "error")
    val error: Error? = null

    @Json(name = "id")
    val id: String? = null

    fun setResult(result: T) {
        this.result = result
    }
}
