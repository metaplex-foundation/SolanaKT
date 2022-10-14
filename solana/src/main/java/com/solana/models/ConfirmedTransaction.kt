package com.solana.models

import com.solana.models.TokenResultObjects.TokenAmountInfo
import com.squareup.moshi.JsonClass

@Deprecated("Use the Kotlin serializer object")
@JsonClass(generateAdapter = true)
data class ConfirmedTransaction(
    val meta: Meta?,
    val slot: Long?,
    val transaction: Transaction?,
) {
    @JsonClass(generateAdapter = true)
    data class Header (
        val numReadonlySignedAccounts: Long,
        val numReadonlyUnsignedAccounts: Long,
        val numRequiredSignatures: Long
    )

    @JsonClass(generateAdapter = true)
    data class Instruction (
        val accounts: List<Long>?,
        val data: String?,
        val programIdIndex: Long
    )
    @JsonClass(generateAdapter = true)
    data class Message (
        val accountKeys: List<String>,
        val header: Header,
        val instructions: List<Instruction>,
        val recentBlockhash: String
    )

    @JsonClass(generateAdapter = true)
    data class Status (
        val ok: Any?
    )

    @JsonClass(generateAdapter = true)
    data class TokenBalance (
        val accountIndex: Double,
        val mint: String,
        val uiTokenAmount: TokenAmountInfo
    )

    @JsonClass(generateAdapter = true)
    data class Meta (
        val err: Any?,
        val fee: Long,
        val innerInstructions: List<Any>,
        val preTokenBalances: List<TokenBalance>,
        val postTokenBalances: List<TokenBalance>,
        val postBalances: List<Long>,
        val preBalances: List<Long>,
        val status: Status
    )

    @JsonClass(generateAdapter = true)
    data class Transaction (
        val message: ConfirmedTransaction.Message,
        val signatures: List<String>,
    )
}