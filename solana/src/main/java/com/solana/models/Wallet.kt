package com.solana.models

class Wallet(val pubkey: String,
             val lamports: Long,
             val token: Token,
             val liquidity: Boolean,
             val userInfo: Map<String, Any>? = null)