package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VoteAccounts(
    val current: List<Value>,
    val delinquent: List<Value>,
){
    @JsonClass(generateAdapter = true)
    data class Value (
        val commission: Long,
        val epochVoteAccount: Boolean,
        val epochCredits: List<List<Long>>,
        val nodePubkey: String,
        val lastVote: Long,
        val activatedStake: Long,
        val votePubkey: String?
    )
}