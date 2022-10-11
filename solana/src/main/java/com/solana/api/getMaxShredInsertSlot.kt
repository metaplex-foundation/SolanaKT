package com.solana.api

import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.serializer

class GetMaxShredInsertSlotRequest() : RpcRequestSerializable() {
    override val method: String = "getMaxShredInsertSlot"

}

internal fun GetMaxShredInsertSlotSerializer() = Long.serializer()

suspend fun Api.getMaxShredInsertSlot(): Result<Long> =
    router.makeRequestResult(GetMaxShredInsertSlotRequest(), GetMaxShredInsertSlotSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<Long> // safe cast, null case handled above
        }

fun Api.getMaxShredInsertSlot(onComplete: (Result<Long>) -> Unit) {
    CoroutineScope(dispatcher).launch {
        onComplete(getMaxShredInsertSlot())
    }
}