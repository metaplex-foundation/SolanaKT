package com.solana.api

import com.solana.models.ClusterNode

fun Api.getClusterNodes(onComplete: (Result<List<ClusterNode>>) -> Unit) {
    val params: List<Any> = ArrayList()
    router.request(
        "getClusterNodes", params,
        List::class.java
    ) { result ->   // List<AbstractMap>
        result.map {
            it.filterNotNull()
        }.map { result ->
            result.map { item -> item as Map<String, Any> }
        }.map {
            val result: MutableList<ClusterNode> = ArrayList()
            for (item in it) {
                result.add(ClusterNode(item))
            }
            result
        }
            .onSuccess {
                onComplete(Result.success(it))
            }.onFailure {
                onComplete(Result.failure(it))
            }
    }
}