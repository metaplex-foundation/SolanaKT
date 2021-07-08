package com.solana.models

import com.squareup.moshi.JsonClass

class TokenResultObjects {

    @JsonClass(generateAdapter = true)
    open class TokenAmountInfo(
        var amount: String?,
        var decimals: Int,
        var uiAmount: Double,
        var uiAmountString: String
    )

    @JsonClass(generateAdapter = true)
    data class TokenAccount (
        val amount: String?,
        val decimals: Int,
        val uiAmount: Double?,
        val uiAmountString: String?,
        val address: String?
    )

    @JsonClass(generateAdapter = true)
    data class TokenInfo (
        val isNative: Boolean?,
        val mint: String?,
        val owner: String?,
        val state: String?,
        val tokenAmount: TokenAmountInfo?
    )

    @JsonClass(generateAdapter = true)
    data class ParsedData (
        val info: TokenInfo,
        val type: String
    )

    @JsonClass(generateAdapter = true)
    data class Data (
        val parsed: ParsedData,
        val program: String,
        val space: Int? = null
    )
}