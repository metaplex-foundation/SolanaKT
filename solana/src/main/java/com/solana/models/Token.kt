package com.solana.models

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val _tags: List<String> = listOf(),
    val chainId: Int,
    val address: String,
    val symbol: String,
    val name: String,
    val decimals: Int,
    val logoURI: String? = null,
    val extensions: TokenExtensions? = null,
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

@Serializable
data class TokensList(
    val name: String,
    val logoURI: String,
    val keywords: List<String>,
    val tags: Map<String, TokenTag>,
    val timestamp: String,
    var tokens: List<Token>,
)

@Serializable
data class TokenTag(
    val name: String,
    val description: String
)

@Serializable
data class TokenExtensions(
    val website: String? = null,
    val bridgeContract: String? = null
)
