package com.solana.api

import com.solana.networking.RpcRequestSerializable
import com.solana.networking.SolanaResponseSerializer
import com.solana.networking.makeRequestResult
import kotlinx.serialization.builtins.serializer

class GetFirstAvailableBlockRequest : RpcRequestSerializable() {
    override val method: String = "getFirstAvailableBlock"
}

internal fun GetFirstAvailableBlockSerializer() = Long.serializer()

fun Api.getFirstAvailableBlock(onComplete: ((Result<Long>) -> Unit)){
    router.request("getFirstAvailableBlock", ArrayList(), Long::class.javaObjectType, onComplete)
}

suspend fun Api.getFirstAvailableBlock(): Result<Long> =
    router.makeRequestResult(GetFirstAvailableBlockRequest(), GetFirstAvailableBlockSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<Long> // safe cast, null case handled above
        }