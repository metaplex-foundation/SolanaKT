package com.solana.api

import com.solana.models.FeesInfo

fun Api.getFees(onComplete: ((Result<FeesInfo>) -> Unit)){
    router.call("getFees", ArrayList(), FeesInfo::class.java, onComplete)
}