package com.solana.api

import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray

class GetConfirmedTransactionRequest(signature: String,) : RpcRequestSerializable() {
    override val method: String = "getConfirmedTransaction"
    override val params = buildJsonArray {
        add(signature)
    }
}

internal fun GetConfirmedTransactionSerializer() = ConfirmedTransactionSerializable.serializer()

fun Api.getConfirmedTransaction(signature: String,
                            onComplete: ((Result<ConfirmedTransactionSerializable>) -> Unit)
){
    CoroutineScope(dispatcher).launch {
        onComplete(getConfirmedTransaction(signature))
    }
}

suspend fun Api.getConfirmedTransaction(signature: String): Result<ConfirmedTransactionSerializable> =
    router.makeRequestResult(GetConfirmedTransactionRequest(signature), GetConfirmedTransactionSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<ConfirmedTransactionSerializable> // safe cast, null case handled above
        }