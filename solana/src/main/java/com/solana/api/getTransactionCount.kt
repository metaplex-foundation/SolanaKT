package com.solana.api

import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.serializer

class GetTransactionCountRequest : RpcRequestSerializable() {
    override val method: String = "getTransactionCount"
}

internal fun GetTransactionCountSerializer() = Long.serializer()

fun Api.getTransactionCount(onComplete: ((Result<Long>) -> Unit)) {
    CoroutineScope(dispatcher).launch {
        onComplete(getSnapshotSlot())
    }
}

suspend fun Api.getTransactionCount(): Result<Long> =
    router.makeRequestResult(GetTransactionCountRequest(), GetTransactionCountSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<Long> // safe cast, null case handled above
        }