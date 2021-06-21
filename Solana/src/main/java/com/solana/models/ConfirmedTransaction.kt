package com.solana.models

import com.squareup.moshi.Json

class ConfirmedTransaction {
    class Header {
        @Json(name = "numReadonlySignedAccounts")
        val numReadonlySignedAccounts: Long = 0

        @Json(name = "numReadonlyUnsignedAccounts")
        val numReadonlyUnsignedAccounts: Long = 0

        @Json(name = "numRequiredSignatures")
        val numRequiredSignatures: Long = 0
    }

    class Instruction {
        @Json(name = "accounts")
        val accounts: List<Long>? = null

        @Json(name = "data")
        val data: String? = null

        @Json(name = "programIdIndex")
        val programIdIndex: Long = 0
    }

    class Message {
        @Json(name = "accountKeys")
        val accountKeys: List<String>? = null

        @Json(name = "header")
        val header: Header? = null

        @Json(name = "instructions")
        val instructions: List<Instruction>? = null

        @Json(name = "recentBlockhash")
        val recentBlockhash: String? = null
    }

    class Status {
        @Json(name = "Ok")
        val ok: Any? = null
    }

    class Meta {
        @Json(name = "err")
        val err: Any? = null

        @Json(name = "fee")
        val fee: Long = 0

        @Json(name = "innerInstructions")
        val innerInstructions: List<Any>? = null

        @Json(name = "postBalances")
        val postBalances: List<Long>? = null

        @Json(name = "preBalances")
        val preBalances: List<Long>? = null

        @Json(name = "status")
        val status: Status? = null
    }

    class Transaction {
        @Json(name = "message")
        val message: Message? = null

        @Json(name = "signatures")
        val signatures: List<String>? = null
    }

    @Json(name = "meta")
    val meta: Meta? = null

    @Json(name = "slot")
    val slot: Long = 0

    @Json(name = "transaction")
    val transaction: Transaction? = null
}