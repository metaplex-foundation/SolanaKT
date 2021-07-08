package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Wallet(
    val pubkey: String,
    val lamports: Long,
    //val token: Token,
    val liquidity: Boolean,
    val userInfo: Map<String, Any>? = null
)