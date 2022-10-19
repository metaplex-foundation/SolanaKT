package com.solana.api

import com.solana.core.PublicKey
import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import com.solana.networking.serialization.serializers.solana.PublicKeyAsStringSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GetSlotLeaderRequest : RpcRequestSerializable() {
    override val method: String = "getSlotLeader"
}

internal fun GetSlotLeaderSerializer() = PublicKeyAsStringSerializer

suspend fun Api.getSlotLeader(): Result<PublicKey> =
    router.makeRequestResult(GetSlotLeaderRequest(), GetSlotLeaderSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<PublicKey> // safe cast, null case handled above
        }

fun Api.getSlotLeader(onComplete: (Result<PublicKey>) -> Unit) {
    CoroutineScope(dispatcher).launch {
        onComplete(getSlotLeader())
    }
}