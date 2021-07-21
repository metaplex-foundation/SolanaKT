package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.StakeActivation
import com.solana.models.StakeActivationConfig

fun Api.getStakeActivation(publicKey: PublicKey, onComplete: ((Result<StakeActivation>) -> Unit)) {
    val params: MutableList<Any> = ArrayList()
    params.add(publicKey.toBase58())
    router.request("getStakeActivation", params, StakeActivation::class.java, onComplete)
}

fun Api.getStakeActivation(publicKey: PublicKey, epoch: Long, onComplete: ((Result<StakeActivation>) -> Unit)) {
    val params: MutableList<Any> = ArrayList()
    params.add(publicKey.toBase58())
    params.add(StakeActivationConfig(epoch))
    router.request("getStakeActivation", params, StakeActivation::class.java, onComplete)
}