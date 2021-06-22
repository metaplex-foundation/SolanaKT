package com.solana.models

import com.squareup.moshi.Json

class VoteAccounts {
    class VoteAccountValue {
        @Json(name = "commission")
        private val commission: Long = 0

        @Json(name = "epochVoteAccount")
        private val epochVoteAccount = false

        @Json(name = "epochCredits")
        private val epochCredits: List<List<Long>>? = null

        @Json(name = "nodePubkey")
        private val nodePubkey: String? = null

        @Json(name = "lastVote")
        private val lastVote: Long = 0

        @Json(name = "activatedStake")
        private val activatedStake: Long = 0

        @Json(name = "votePubkey")
        private val votePubkey: String? = null
    }

    @Json(name = "current")
    private val current: List<VoteAccountValue>? = null

    @Json(name = "delinquent")
    private val delinquent: List<VoteAccountValue>? = null
}