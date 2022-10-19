package com.solana.api

import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray


class GetBlockCommitmentRequest(block: Long) : RpcRequestSerializable() {
    override val method: String = "getBlockCommitment"
    override val params = buildJsonArray {
        add(block)
    }
}

@Serializable
data class GetBlockCommitmentResponse(var commitment: LongArray?, var totalStake: Long)

internal fun GetBlockCommitmentSerializer() = GetBlockCommitmentResponse.serializer()

fun Api.getBlockCommitment(block: Long, onComplete: ((Result<GetBlockCommitmentResponse>) -> Unit)) {
    CoroutineScope(dispatcher).launch {
        onComplete(getBlockCommitment(block))
    }
}

suspend fun Api.getBlockCommitment(block: Long): Result<GetBlockCommitmentResponse> =
    router.makeRequestResult(GetBlockCommitmentRequest(block), GetBlockCommitmentSerializer()).let { result ->
        @Suppress("UNCHECKED_CAST")
        if (result.isSuccess && result.getOrNull() == null)
            Result.failure(Error("Can not be null"))
        else result as Result<GetBlockCommitmentResponse>// safe cast, null case handled above
    }