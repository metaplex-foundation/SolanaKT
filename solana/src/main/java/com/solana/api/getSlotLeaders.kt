package com.solana.api

import com.solana.core.PublicKey
import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import com.solana.networking.serialization.serializers.solana.PublicKeyAsStringSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray

class GetSlotLeadersRequest(startSlot: Long, limit: Long) : RpcRequestSerializable() {
    override val method: String = "getSlotLeaders"
    override val params = buildJsonArray {
        add(startSlot)
        add(limit)
    }
}

internal fun GetSlotLeadersSerializer() = ListSerializer(PublicKeyAsStringSerializer)

suspend fun Api.getSlotLeaders(startSlot: Long, limit: Long): Result<List<PublicKey>> =
    router.makeRequestResult(GetSlotLeadersRequest(startSlot, limit), GetSlotLeadersSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<List<PublicKey>> // safe cast, null case handled above
        }

fun Api.getSlotLeaders(startSlot: Long, limit: Long,
                   onComplete: (Result<List<PublicKey>>) -> Unit
) {
    CoroutineScope(dispatcher).launch {
        onComplete(getSlotLeaders(startSlot, limit))
    }
}