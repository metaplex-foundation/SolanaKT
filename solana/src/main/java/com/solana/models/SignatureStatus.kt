package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignatureStatusRequestConfiguration(
    var searchTransactionHistory: Boolean? = null
)

@JsonClass(generateAdapter = true)
data class SignatureStatus(
    override val value: List<Value>
) : RPC<List<SignatureStatus.Value>>(null, value) {
    @JsonClass(generateAdapter = true)
    data class Value(
        val slot: Long,
        val confirmations: Long?,
        var err: Any?,
        var confirmationStatus: String?
    )
}

