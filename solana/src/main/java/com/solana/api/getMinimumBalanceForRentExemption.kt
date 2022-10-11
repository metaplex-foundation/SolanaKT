package com.solana.api

import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray


class GetMinimumBalanceForRentExemptionRequest(dataLength: Long) : RpcRequestSerializable() {
    override val method: String = "getMinimumBalanceForRentExemption"
    override val params = buildJsonArray {
        add(dataLength)
    }
}

internal fun GetMinimumBalanceForRentExemptionSerializer() = Long.serializer()

suspend fun Api.getMinimumBalanceForRentExemption(dataLength: Long): Result<Long> =
    router.makeRequestResult(GetMinimumBalanceForRentExemptionRequest(dataLength), GetMinimumBalanceForRentExemptionSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<Long> // safe cast, null case handled above
        }

fun Api.getMinimumBalanceForRentExemption(dataLength: Long,  onComplete: ((Result<Long>) -> Unit)) {
    CoroutineScope(dispatcher).launch {
        onComplete(getMinimumBalanceForRentExemption(dataLength))
    }
}