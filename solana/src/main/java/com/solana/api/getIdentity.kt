package com.solana.api

import com.solana.core.PublicKey
import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import com.solana.networking.serialization.serializers.solana.PublicKeyAsStringSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class GetIdentityBlockRequest : RpcRequestSerializable() {
    override val method: String = "getIdentity"
}

@Serializable
data class GetIdentityResponse (
    @Serializable(with = PublicKeyAsStringSerializer::class) val identity: PublicKey
)

internal fun GetIdentitySerializer() = GetIdentityResponse.serializer()

fun Api.getIdentity(onComplete: (Result<PublicKey>) -> Unit) {
    CoroutineScope(dispatcher).launch {
        onComplete(getIdentity())
    }
}

suspend fun Api.getIdentity(): Result<PublicKey> =
    router.makeRequestResult(GetIdentityBlockRequest(), GetIdentitySerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result.map {
                it!!.identity
            } as Result<PublicKey> // safe cast, null case handled above
        }