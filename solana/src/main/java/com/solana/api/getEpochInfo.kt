package com.solana.api

import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class GetEpochInfoRequest : RpcRequestSerializable() {
    override val method: String = "getEpochInfo"
}

@Serializable
data class EpochInfo (
    val absoluteSlot: Long,
    val blockHeight: Long,
    val epoch: Long,
    val slotIndex: Long,
    val slotsInEpoch: Long
)

internal fun GetEpochInfoSerializer() = EpochInfo.serializer()

fun Api.getEpochInfo(onComplete: ((Result<EpochInfo>) -> Unit)) {
    CoroutineScope(dispatcher).launch {
        onComplete(getEpochInfo())
    }
}

suspend fun Api.getEpochInfo(): Result<EpochInfo> =
    router.makeRequestResult(GetEpochInfoRequest(), GetEpochInfoSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<EpochInfo> // safe cast, null case handled above
        }