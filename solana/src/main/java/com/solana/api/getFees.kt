package com.solana.api

import com.solana.models.FeesInfo

fun Api.getFees(onComplete: ((Result<FeesInfo>) -> Unit)){
    router.request("getFees", ArrayList(), FeesInfo::class.java, onComplete)
}