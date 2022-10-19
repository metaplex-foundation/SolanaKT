package com.solana.api

import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.serializer

class GetSlotRequest : RpcRequestSerializable() {
    override val method: String = "getSlot"
}

internal fun GetSlotSerializer() = Long.serializer()

suspend fun Api.getSlot(): Result<Long> =
    router.makeRequestResult(GetSlotRequest(), GetSlotSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<Long> // safe cast, null case handled above
        }

fun Api.getSlot(onComplete: (Result<Long>) -> Unit) {
    CoroutineScope(dispatcher).launch {
        onComplete(getSlot())
    }
}