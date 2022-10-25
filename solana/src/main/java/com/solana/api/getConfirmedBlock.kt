package com.solana.api

import com.solana.models.RpcSendTransactionConfig
import com.solana.networking.RpcRequest
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

class GetConfirmedBlockRequest(
    slot: Long,
    encoding: RpcSendTransactionConfig.Encoding = RpcSendTransactionConfig.Encoding.base64,
    transactionDetails: String = "full",
    rewards: Boolean = true,
    commitment: String = "finalized"
) : RpcRequest() {
    override val method: String = "getConfirmedBlock"
    override val params = buildJsonArray {
        add(slot)
        addJsonObject {
            put("encoding", encoding.getEncoding())
            put("transactionDetails", transactionDetails)
            put("rewards", rewards)
            put("commitment", commitment)
        }
    }
}

@Serializable
data class ConfirmedBlock(
    val blockTime: Long,
    val blockhash: String?,
    val parentSlot: Long,
    val previousBlockhash: String?,
    val transactions: List<ConfirmedTransactionSerializable>?
)

internal fun GetConfirmedBlockSerializer() = ConfirmedBlock.serializer()

fun Api.getConfirmedBlock(slot: Int, onComplete: (Result<ConfirmedBlock>) -> Unit) {
    CoroutineScope(dispatcher).launch {
        onComplete(getConfirmedBlock(slot.toLong()))
    }
}

suspend fun Api.getConfirmedBlock(
    slot: Long,
    encoding: RpcSendTransactionConfig.Encoding = RpcSendTransactionConfig.Encoding.base64,
    transactionDetails: String = "full",
    rewards: Boolean = true,
    commitment: String = "finalized"
): Result<ConfirmedBlock> =
    router.makeRequestResult(
        GetConfirmedBlockRequest(
            slot,
            encoding,
            transactionDetails,
            rewards,
            commitment
        ), GetConfirmedBlockSerializer()
    )
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null) {
                Result.failure(Error("Can not be null"))
            } else {
                result as Result<ConfirmedBlock> // safe cast, null case handled above
            }
        }