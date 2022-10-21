package com.solana.api

import com.solana.core.PublicKey
import com.solana.networking.RpcRequest
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.add
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.put

class GetStakeActivationRequest(publicKey: PublicKey, epoch: Long? = null) : RpcRequest() {
    override val method: String = "getStakeActivation"
    override val params = buildJsonArray {
        add(publicKey.toBase58())
        epoch?.let {
            addJsonObject {
                put("epoch", epoch)
            }
        }
    }
}

@Serializable
data class StakeActivation (
    val active: Long,
    val inactive: Long,
    val state: String
)

internal fun GetStakeActivationSerializer() = StakeActivation.serializer()

suspend fun Api.getStakeActivation(publicKey: PublicKey, epoch: Long? = null): Result<StakeActivation> =
    router.makeRequestResult(GetStakeActivationRequest(publicKey, epoch), GetStakeActivationSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<StakeActivation> // safe cast, null case handled above
        }

fun Api.getStakeActivation(publicKey: PublicKey, onComplete: ((Result<StakeActivation>) -> Unit)) {
    CoroutineScope(dispatcher).launch {
        onComplete(getStakeActivation(publicKey))
    }
}

fun Api.getStakeActivation(publicKey: PublicKey, epoch: Long, onComplete: ((Result<StakeActivation>) -> Unit)) {
    CoroutineScope(dispatcher).launch {
        onComplete(getStakeActivation(publicKey, epoch))
    }
}