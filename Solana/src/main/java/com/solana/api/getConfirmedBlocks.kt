package com.solana.api

fun Api.getConfirmedBlocks(start: Int, end: Int? = null, onComplete: (Result<List<Double>>) -> Unit) {
    val params: List<Int>
    params = if (end == null) listOf(start) else listOf(start, end)
    router.request("getConfirmedBlocks", params, List::class.java) { result ->
        result.map { list ->
            list.map { it as Double }
        }.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}