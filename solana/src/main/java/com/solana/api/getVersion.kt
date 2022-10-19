package com.solana.api

import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class GetVersionRequest : RpcRequestSerializable() {
    override val method: String = "getVersion"
}

@Serializable
data class SolanaVersion (
    @SerialName("solana-core")
    val solanaCore: String,

    @SerialName("feature-set")
    val featureSet: Long
)

internal fun GetVersionSerializer() = SolanaVersion.serializer()

fun Api.getVersion(onComplete: ((Result<SolanaVersion>) -> Unit)) {
    CoroutineScope(dispatcher).launch {
        onComplete(getVersion())
    }
}

suspend fun Api.getVersion(): Result<SolanaVersion> =
    router.makeRequestResult(GetVersionRequest(), GetVersionSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<SolanaVersion> // safe cast, null case handled above
        }