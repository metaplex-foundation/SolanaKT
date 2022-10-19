package com.solana.api

import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.serializer

class GetGenesisHashBlockRequest : RpcRequestSerializable() {
    override val method: String = "getGenesisHash"
}

internal fun GetGenesisHashBlockSerializer() = String.serializer()

fun Api.getGenesisHash(onComplete: ((Result<String>) -> Unit)){
    CoroutineScope(dispatcher).launch {
        onComplete(getGenesisHash())
    }
}

suspend fun Api.getGenesisHash(): Result<String> =
    router.makeRequestResult(GetGenesisHashBlockRequest(), GetGenesisHashBlockSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<String> // safe cast, null case handled above
        }