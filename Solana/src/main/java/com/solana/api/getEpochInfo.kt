package com.solana.api

import com.solana.models.EpochInfo

fun Api.getEpochInfo(onComplete: ((Result<EpochInfo>) -> Unit)) {
    val params: List<Any> = ArrayList()
    router.request("getEpochInfo", params, EpochInfo::class.java, onComplete)
}