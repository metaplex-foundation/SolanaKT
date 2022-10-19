package com.solana.api

import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray

class GetBlockTimeRequest(block: Long) : RpcRequestSerializable() {
    override val method: String = "getBlockTime"
    override val params = buildJsonArray {
        add(block)
    }
}

internal fun GetBlockTimeSerializer() = Long.serializer()

fun Api.getBlockTime(block: Long, onComplete: ((Result<Long>) -> Unit)) {
    CoroutineScope(dispatcher).launch {
        onComplete(getBlockTime(block))
    }
}

suspend fun Api.getBlockTime(block: Long): Result<Long> =
    router.makeRequestResult(GetBlockTimeRequest(block), GetBlockTimeSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<Long> // safe cast, null case handled above
        }