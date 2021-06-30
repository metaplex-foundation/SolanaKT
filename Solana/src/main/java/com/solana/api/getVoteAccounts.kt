package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.VoteAccountConfig
import com.solana.models.VoteAccounts

fun Api.getVoteAccounts(votePubkey: PublicKey? = null, onComplete: ((Result<VoteAccounts>) -> Unit)) {
    val params: MutableList<Any> = ArrayList()
    if (votePubkey != null) {
        params.add(VoteAccountConfig(votePubkey.toBase58()))
    }
    router.request("getVoteAccounts", params, VoteAccounts::class.java, onComplete)
}