package com.solana.api

import com.solana.models.FeeCalculatorInfo

fun Api.getFeeCalculatorForBlockhash(blockhash: String, onComplete: ((Result<FeeCalculatorInfo>) -> Unit)) {
    val params: MutableList<Any> = ArrayList()
    params.add(blockhash)
    router.call("getFeeCalculatorForBlockhash", params, FeeCalculatorInfo::class.java, onComplete)
}
