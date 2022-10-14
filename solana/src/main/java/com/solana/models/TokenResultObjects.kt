package com.solana.models

import com.squareup.moshi.Json

@Deprecated("Please use the Koltin Serializer Objects")
class TokenResultObjects {
    open class TokenAmountInfo(
        @Json(name = "amount") var amount: String?,
        @Json(name = "decimals") var decimals: Int,
        @Json(name = "uiAmount") var uiAmount: Double?,
        @Json(name = "uiAmountString") var uiAmountString: String
    )

    class TokenInfo (
        @Json(name = "isNative")
        private val isNative: Boolean?,

        @Json(name = "mint")
        private val mint: String?,

        @Json(name = "owner")
        private val owner: String?,

        @Json(name = "state")
        private val state: String?,

        @Json(name = "tokenAmount")
        private val tokenAmount: TokenAmountInfo?
    )

    class ParsedData (
        @Json(name = "info")
        private val info: TokenInfo,

        @Json(name = "type")
        private val type: String
    )
}