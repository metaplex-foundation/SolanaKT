package com.solana.vendor

import com.solana.models.Token
import com.solana.models.TokenTag
import com.solana.models.TokensList
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File
import java.lang.Exception

class TokensListParser {
    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory()).build()
    }

    fun parse(network: String): Result<List<Token>, ResultError> {
        val sanitizedNetwork = network.replace("-", "_")
        val file = readFileAsTextUsingInputStream("src/main/res/raw/${sanitizedNetwork}_tokens.json")
        val adapter: JsonAdapter<TokensList> = moshi.adapter(TokensList::class.java)
        return try {
            val tokenList = adapter.fromJson(file)!!
            tokenList.tokens = tokenList.tokens.map {
                it.tokenTags = it._tags.map { index ->
                    tokenList.tags.get(index) ?: TokenTag(it.name, it.name)
                }
                it
            }
            val listTokens =  tokenList.tokens.fold(listOf()) { list: List<Token>, token: Token ->
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

    private fun readFileAsTextUsingInputStream(fileName: String) = File(fileName).inputStream().readBytes().toString(Charsets.UTF_8)
}