package com.solana.models

import com.squareup.moshi.Json
import java.util.*

class TokenResultObjects {
    open class TokenAmountInfo(am: AbstractMap<*, *>) {
        @Json(name = "amount")
        private val amount: String?

        @Json(name = "decimals")
        private val decimals: Int

        @Json(name = "uiAmount")
        private val uiAmount: Double?

        @Json(name = "uiAmountString")
        private val uiAmountString: String?

        init {
            amount = am["amount"] as String?
            decimals = (am["decimals"] as Double).toInt()
            uiAmount = am["uiAmount"] as Double?
            uiAmountString = am["uiAmountString"] as String?
        }
    }

    class TokenAccount(am: AbstractMap<*, *>) : TokenAmountInfo(am) {
        @Json(name = "address")
        private val address: String?

        init {
            address = am["address"] as String?
        }
    }

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