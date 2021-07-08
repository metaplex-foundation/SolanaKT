package com.solana.models

import com.solana.models.TokenResultObjects.TokenAmountInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConfirmedTransaction(
    val meta: Meta?,
    val slot: Long?,
    val transaction: Transaction?,
) {
    class Header (
        @Json(name = "numReadonlySignedAccounts")
        val numReadonlySignedAccounts: Long,

        @Json(name = "numReadonlyUnsignedAccounts")
        val numReadonlyUnsignedAccounts: Long,

        @Json(name = "numRequiredSignatures")
        val numRequiredSignatures: Long
    )

    class Instruction (
        @Json(name = "accounts")
        val accounts: List<Long>?,

        @Json(name = "data")
        val data: String?,

        @Json(name = "programIdIndex")
        val programIdIndex: Long
    )

    class Message (
        @Json(name = "accountKeys")
        val accountKeys: List<String>,

        @Json(name = "header")
        val header: Header,

        @Json(name = "instructions")
        val instructions: List<Instruction>,

        @Json(name = "recentBlockhash")
        val recentBlockhash: String
    )

    class Status (
        @Json(name = "Ok")
        val ok: Any?
    )

    class TokenBalance (
        @Json(name = "accountIndex")
        val accountIndex: Double,

        @Json(name = "mint")
        val mint: String,

        @Json(name = "uiTokenAmount")
        val uiTokenAmount: TokenAmountInfo
    )

    class Meta (
        @Json(name = "err")
        val err: Any?,

        @Json(name = "fee")
        val fee: Long,

        @Json(name = "innerInstructions")
        val innerInstructions: List<Any>,

        @Json(name = "preTokenBalances")
        val preTokenBalances: List<TokenBalance>,

        @Json(name = "postTokenBalances")
        val postTokenBalances: List<TokenBalance>,

        @Json(name = "postBalances")
        val postBalances: List<Long>,

        @Json(name = "preBalances")
        val preBalances: List<Long>,

        @Json(name = "status")
        val status: Status
    )

    class Transaction (
        @Json(name = "message")
        val message: ConfirmedTransaction.Message,

        @Json(name = "signatures")
        val signatures: List<String>,
    )
}