package com.solana.models

import com.squareup.moshi.Json
import java.util.*

class TokenResultObjects {
    open class TokenAmountInfo (
        @Json(name = "amount")
        val amount: String?,

        @Json(name = "decimals")
        val decimals: Int,

        @Json(name = "uiAmount")
        val uiAmount: Double?,

        @Json(name = "uiAmountString")
        val uiAmountString: String?
    )

    class TokenAccount(
        @Json(name = "amount")
        val amount: String?,

        @Json(name = "decimals")
        val decimals: Int,

        @Json(name = "uiAmount")
        val uiAmount: Double?,

        @Json(name = "uiAmountString")
        val uiAmountString: String?,

        @Json(name = "address")
        val address: String?
    )

    class TokenInfo {
        @Json(name = "isNative")
        private val isNative: Boolean? = null

        @Json(name = "mint")
        private val mint: String? = null

        @Json(name = "owner")
        private val owner: String? = null

        @Json(name = "state")
        private val state: String? = null

        @Json(name = "tokenAmount")
        private val tokenAmount: TokenAmountInfo? = null
    }

    class ParsedData {
        @Json(name = "info")
        private val info: TokenInfo? = null

        @Json(name = "type")
        private val type: String? = null
    }

    class Data {
        @Json(name = "parsed")
        private val parsed: ParsedData? = null

        @Json(name = "program")
        private val program: String? = null

        @Json(name = "space")
        private val space: Int? = null
    }

    class Value {
        @Json(name = "data")
        private val data: Data? = null

        @Json(name = "executable")
        private val executable = false

        @Json(name = "lamports")
        private val lamports: Long = 0

        @Json(name = "owner")
        private val owner: String? = null

        @Json(name = "rentEpoch")
        private val rentEpoch: Long = 0
    }
}