package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.InflationReward
import com.solana.models.RpcEpochConfig

fun Api.getInflationReward(
    addresses: List<PublicKey>,
    epoch: Long? = null,
    commitment: String? = null,
    onComplete: (Result<List<InflationReward>>) -> Unit
) {
    val params: MutableList<Any> = ArrayList()
    params.add(addresses.map(PublicKey::toString))

    epoch?.let {
        params.add(RpcEpochConfig(it, commitment))
    }

    router.call(
        "getInflationReward", params,
        List::class.java
    ){ result ->
        result.map {
            it.filterNotNull()
        }.map {
            it.map { item -> item as Map<String, Any> }
        }.map {
            val list: MutableList<InflationReward> = ArrayList()
            for (item in it) {
                list.add(InflationReward(item))
            }
            list
        }.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}