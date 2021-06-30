package com.solana.api

import com.solana.core.PublicKey

fun Api.getSlotLeaders(startSlot: Long,
                   limit: Long,
                   onComplete: (Result<List<PublicKey>>) -> Unit
) {
    val params: MutableList<Any> = ArrayList()
    params.add(startSlot)
    params.add(limit)
    router.request(
        "getSlotLeaders", params,
        List::class.java
    ){ result ->
        result.map {
            it.filterNotNull()
        }.map {
            it.map { item -> item as String }
        }.map {
            val list: MutableList<PublicKey> = ArrayList()
            for (item in it) {
                list.add(PublicKey(item))
            }
            list
        }.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}