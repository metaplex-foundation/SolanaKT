package com.solana.vendor

import com.solana.Solana
import com.solana.models.Token
import com.solana.models.TokenTag
import com.solana.models.TokensList
import com.solana.resources.devnet
import com.solana.resources.mainnet_beta
import com.solana.resources.testnet
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


class TokensListParser {
    private val tokens by lazy {
        mapOf(
            "devnet" to devnet,
            "mainnet-beta" to mainnet_beta,
            "testnet" to testnet
        )
    }
    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory()).build()
    }

    fun parse(network: String): Result<List<Token>, ResultError> {
        val jsonContent = tokens[network]
        val adapter: JsonAdapter<TokensList> = moshi.adapter(TokensList::class.java)
        return try {
            val tokenList = adapter.fromJson(jsonContent)!!
            tokenList.tokens = tokenList.tokens.map {
                it.tokenTags = it._tags.map { index ->
                    tokenList.tags.get(index) ?: TokenTag(it.name, it.name)
                }
                it
            }
            val listTokens = tokenList.tokens.fold(listOf()) { list: List<Token>, token: Token ->
                var result = list.toMutableList()
                if (!result.contains(token)) {
                    result.add(token)
                }
                result.toList()
            }
            Result.success(listTokens)
        } catch (e: Exception) {
            Result.failure(ResultError(e))
        }
    }
}