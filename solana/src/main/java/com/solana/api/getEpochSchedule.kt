package com.solana.api

import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class GetEpochScheduleRequest : RpcRequestSerializable() {
    override val method: String = "getEpochSchedule"
}

@Serializable
data class EpochSchedule(
    val slotsPerEpoch: Long,
    val leaderScheduleSlotOffset: Long,
    val warmup: Boolean,
    val firstNormalEpoch: Long,
    val firstNormalSlot: Long
)

internal fun GetEpochScheduleSerializer() = EpochSchedule.serializer()

fun Api.getEpochSchedule(onComplete: ((Result<EpochSchedule>) -> Unit)) {
    CoroutineScope(dispatcher).launch {
        onComplete(getEpochSchedule())
    }
}

suspend fun Api.getEpochSchedule(): Result<EpochSchedule> =
    router.makeRequestResult(GetEpochScheduleRequest(), GetEpochScheduleSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<EpochSchedule> // safe cast, null case handled above
        }