package com.solana.models

import com.squareup.moshi.Json
import java.util.*

class TokenResultObjects {
    open class TokenAmountInfo(am: Map<String, Any>) {

        @Json(name = "amount")
        var amount: String? = am["amount"] as String

        @Json(name = "decimals")
        var decimals: Int = (am["decimals"] as Double).toInt()

        @Json(name = "uiAmount")
        var uiAmount: Double = am["uiAmount"] as Double

        @Json(name = "uiAmountString")
        var uiAmountString: String = am["uiAmountString"] as String

    }

    class TokenAccount (am: Map<String, Any>) {
        @Json(name = "amount")
        val amount: String?

        @Json(name = "decimals")
        val decimals: Int

        @Json(name = "uiAmount")
        val uiAmount: Double?

        @Json(name = "uiAmountString")
        val uiAmountString: String?

        @Json(name = "address")
        val address: String?

        init {
            amount = am["amount"] as String
            decimals = (am["decimals"] as Double).toInt()
            uiAmount = am["uiAmount"] as Double
            uiAmountString = am["uiAmountString"] as String
            address = am["uiAmountString"] as String
        }
    }

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

    class Data (
        @Json(name = "parsed")
        private val parsed: ParsedData,

        @Json(name = "program")
        private val program: String,

        @Json(name = "space")
        private val space: Int? = null
    )

    class Value (
        @Json(name = "data")
        private val data: Data,

        @Json(name = "executable")
        private val executable: Boolean,

        @Json(name = "lamports")
        private val lamports: Long,

        @Json(name = "owner")
        private val owner: String,

        @Json(name = "rentEpoch")
        private val rentEpoch: Long = 0
    )
}