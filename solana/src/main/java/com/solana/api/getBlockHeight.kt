package com.solana.api

import com.solana.networking.RpcRequest
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.serializer

class GetBlockHeightRequest : RpcRequest() {
    override val method: String = "getBlockHeight"
}

internal fun GetBlockHeightSerializer() = Long.serializer()

fun Api.getBlockHeight(onComplete: ((Result<Long>) -> Unit)) {
    CoroutineScope(dispatcher).launch {
        onComplete(getBlockHeight())
    }
}

suspend fun Api.getBlockHeight(): Result<Long> =
    router.makeRequestResult(GetBlockHeightRequest(), GetBlockHeightSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<Long> // safe cast, null case handled above
        }