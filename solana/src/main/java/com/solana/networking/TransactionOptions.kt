package com.solana.networking

import com.solana.models.RpcSendTransactionConfig
import java.time.Duration

typealias Encoding = RpcSendTransactionConfig.Encoding

enum class Commitment(val value: String) {
    PROCESSED("processed"),
    CONFIRMED("confirmed"),
    FINALIZED("finalized"),
    MAX("max"),
    RECENT("recent");

    override fun toString(): String {
        return value
    }
}

data class TransactionOptions(
    val commitment: Commitment = Commitment.FINALIZED,
    val encoding: Encoding = Encoding.base64,
    val skipPreflight: Boolean = false,
    val preflightCommitment: Commitment = commitment,
    val timeout: Int = 30
)