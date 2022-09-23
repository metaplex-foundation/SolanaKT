package com.solana.api

import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray

class GetConfirmedBlocksRequest(start: Long, end: Long? = null) : RpcRequestSerializable() {
    override val method: String = "getConfirmedBlocks"
    override val params = buildJsonArray {
        add(start)
        end?.let { add(end) }
    }
}

internal fun GetConfirmedBlocksSerializer() = ListSerializer(Long.serializer())

fun Api.getConfirmedBlocks(start: Int, end: Int? = null, onComplete: (Result<List<Double>>) -> Unit) {
    CoroutineScope(dispatcher).launch {
        onComplete(getConfirmedBlocks(start.toLong(), end?.toLong()))
    }
}

suspend fun Api.getConfirmedBlocks(start: Long, end: Long? = null,): Result<List<Double>> =
    router.makeRequestResult(GetConfirmedBlocksRequest(start, end), GetConfirmedBlocksSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<List<Double>> // safe cast, null case handled above
        }