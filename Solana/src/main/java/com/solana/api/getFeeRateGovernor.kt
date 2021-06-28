package com.solana.api

import com.solana.models.FeeRateGovernorInfo

fun Api.getFeeRateGovernor(onComplete: ((Result<FeeRateGovernorInfo>) -> Unit)) {
    router.call("getFeeRateGovernor", ArrayList(), FeeRateGovernorInfo::class.java, onComplete)
}