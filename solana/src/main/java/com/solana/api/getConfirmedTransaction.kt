package com.solana.api

import com.solana.models.ConfirmedTransaction

fun Api.getConfirmedTransaction(signature: String,
                            onComplete: ((Result<ConfirmedTransaction>) -> Unit)
){
    val params: MutableList<Any> = ArrayList()
    params.add(signature)
    return router.request("getConfirmedTransaction", params, ConfirmedTransaction::class.java, onComplete)
}