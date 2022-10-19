package com.solana.api

import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.serializer

class GetMaxRetransmitSlotRequest() : RpcRequestSerializable() {
    override val method: String = "getMaxRetransmitSlot"

}

internal fun GetMaxRetransmitSlotSerializer() = Long.serializer()

suspend fun Api.getMaxRetransmitSlot(): Result<Long> =
    router.makeRequestResult(GetMaxRetransmitSlotRequest(), GetMaxRetransmitSlotSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<Long> // safe cast, null case handled above
        }

fun Api.getMaxRetransmitSlot(onComplete: ((Result<Long>) -> Unit)) {
    CoroutineScope(dispatcher).launch {
        onComplete(getMaxRetransmitSlot())
    }
}

