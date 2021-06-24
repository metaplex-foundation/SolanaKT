package com.solana.models

import com.squareup.moshi.Json

class VoteAccounts(
    @Json(name = "current")
    private val current: List<Value>,

    @Json(name = "delinquent")
    private val delinquent: List<Value>,
){
    class Value (
        @Json(name = "commission")
        val commission: Long,

        @Json(name = "epochVoteAccount")
        val epochVoteAccount: Boolean,

        @Json(name = "epochCredits")
        val epochCredits: List<List<Long>>,

        @Json(name = "nodePubkey")
        val nodePubkey: String,

        @Json(name = "lastVote")
        val lastVote: Long,

        @Json(name = "activatedStake")
        val activatedStake: Long,

        @Json(name = "votePubkey")
        val votePubkey: String?
    )
}