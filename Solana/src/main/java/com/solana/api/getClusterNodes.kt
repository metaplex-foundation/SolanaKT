package com.solana.api

import com.solana.models.ClusterNode

fun Api.getClusterNodes(onComplete: (Result<List<ClusterNode>>) -> Unit) {
    val params: List<Any> = ArrayList()
    router.request<List<ClusterNode>>(
        "getClusterNodes", params,
        List::class.java
    ) { result ->   // List<AbstractMap>
        result.onSuccess {
                onComplete(Result.success(it))
            }.onFailure {
                onComplete(Result.failure(it))
            }
    }
}