package com.solana.models

import com.solana.models.TokenResultObjects.TokenAmountInfo
import com.squareup.moshi.Json

class ConfirmedTransaction(
    @Json(name = "meta") private val meta: Meta?,
    @Json(name = "slot") private val slot: Long,
    @Json(name = "transaction") private val transaction: Transaction?,
) {
    class Header {
        @Json(name = "numReadonlySignedAccounts")
        private val numReadonlySignedAccounts: Long = 0

        @Json(name = "numReadonlyUnsignedAccounts")
        private val numReadonlyUnsignedAccounts: Long = 0

        @Json(name = "numRequiredSignatures")
        private val numRequiredSignatures: Long = 0
    }

    class Instruction {
        @Json(name = "accounts")
        private val accounts: List<Long>? = null

        @Json(name = "data")
        private val data: String? = null

        @Json(name = "programIdIndex")
        private val programIdIndex: Long = 0
    }

    class Message {
        @Json(name = "accountKeys")
        private val accountKeys: List<String>? = null

        @Json(name = "header")
        private val header: Header? = null

        @Json(name = "instructions")
        private val instructions: List<Instruction>? = null

        @Json(name = "recentBlockhash")
        private val recentBlockhash: String? = null
    }

    class Status {
        @Json(name = "Ok")
        private val ok: Any? = null
    }

    class TokenBalance {
        @Json(name = "accountIndex")
        private val accountIndex: Double? = null

        @Json(name = "mint")
        private val mint: String? = null

        @Json(name = "uiTokenAmount")
        private val uiTokenAmount: TokenAmountInfo? = null
    }

    class Meta {
        @Json(name = "err")
        private val err: Any? = null

        @Json(name = "fee")
        private val fee: Long = 0

        @Json(name = "innerInstructions")
        private val innerInstructions: List<Any>? = null

        @Json(name = "preTokenBalances")
        private val preTokenBalances: List<TokenBalance>? = null

        @Json(name = "postTokenBalances")
        private val postTokenBalances: List<TokenBalance>? = null

        @Json(name = "postBalances")
        private val postBalances: List<Long>? = null

        @Json(name = "preBalances")
        private val preBalances: List<Long>? = null

        @Json(name = "status")
        private val status: Status? = null
    }

    class Transaction {
        @Json(name = "message")
        private val message: Message? = null

        @Json(name = "signatures")
        private val signatures: List<String>? = null
    }
}