package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.SignatureStatusRequestConfiguration
import com.solana.networking.RpcRequestSerializable
import com.solana.networking.SolanaResponseSerializer
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.*

class GetSignatureStatusesRequest(signatures: List<String>, configs: SignatureStatusRequestConfiguration? = SignatureStatusRequestConfiguration()) : RpcRequestSerializable() {
    override val method: String = "getSignatureStatuses"
    override val params = buildJsonArray {
        addJsonArray {
            signatures.forEach {
                add(it)
            }
        }
        configs?.let {
            addJsonObject {
                it.searchTransactionHistory?.let { searchTransactionHistory ->
                    put("searchTransactionHistory", searchTransactionHistory)
                }
            }
        }
    }
}

@Serializable
data class SignatureStatus(
    val slot: Long,
    val confirmations: Long?,
    var err: JsonObject?,
    var confirmationStatus: String?
)

internal fun GetSignatureStatusesSerializer() = SolanaResponseSerializer(ListSerializer(SignatureStatus.serializer()))

suspend fun Api.getSignatureStatuses(signatures: List<String>, configs: SignatureStatusRequestConfiguration? = SignatureStatusRequestConfiguration()): Result<List<SignatureStatus>> =
    router.makeRequestResult(GetSignatureStatusesRequest(signatures, configs), GetSignatureStatusesSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<List<SignatureStatus>> // safe cast, null case handled above
        }

fun Api.getSignatureStatuses(signatures: List<String>, configs: SignatureStatusRequestConfiguration? = SignatureStatusRequestConfiguration(), onComplete: ((Result<List<SignatureStatus>>) -> Unit)) {
    CoroutineScope(dispatcher).launch {
        onComplete(getSignatureStatuses(signatures, configs))
    }
}