package com.solana.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Token(
    @Json(name = "tags") val _tags: List<String>,
    val chainId: Int,
    val address: String,
    val symbol: String,
    val name: String,
    val decimals: Int,
    val logoURI: String?,
    val extensions: TokenExtensions?,
    val isNative: Boolean = false,
) {
    var tokenTags: List<TokenTag> = listOf()

    companion object {
        fun unsupported( mint: String?): Token {
            return Token(
                _tags = listOf(),
                chainId = 101,
                address = mint ?: "<undefined>",
                symbol = "",
                name = mint ?: "<undefined>",
                decimals = 0,
                logoURI = null,
                extensions = null
            )
        }
    }
}

@JsonClass(generateAdapter = true)
data class TokensList(
    val name: String,
    val logoURI: String,
    val keywords: List<String>,
    val tags: Map<String, TokenTag>,
    val timestamp: String,
    var tokens: List<Token>,
)

@JsonClass(generateAdapter = true)
data class TokenTag(
    val name: String,
    val description: String
)


data class TokenExtensions(
    val website: String?,
    val bridgeContract: String?
)
