package com.solana.api

import com.solana.models.EpochSchedule

fun Api.getEpochSchedule(onComplete: ((Result<EpochSchedule>) -> Unit)) {
    val params: List<Any> = ArrayList()
    router.call("getEpochSchedule", params, EpochSchedule::class.java, onComplete)
}