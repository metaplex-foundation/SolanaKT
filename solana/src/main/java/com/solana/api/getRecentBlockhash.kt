package com.solana.api

import com.solana.networking.RpcRequestSerializable
import com.solana.networking.SolanaResponseSerializer
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import java.lang.RuntimeException

class RecentBlockhashRequest : RpcRequestSerializable() {
    override val method: String = "getRecentBlockhash"
}

@Serializable
internal data class BlockhashResponse(val blockhash: String, val feeCalculator: JsonElement)

internal fun BlockhashSerializer() = SolanaResponseSerializer(BlockhashResponse.serializer())

suspend fun Api.getRecentBlockhash(): Result<String> =
    router.makeRequestResult(RecentBlockhashRequest(), BlockhashSerializer()).let { result ->
        @Suppress("UNCHECKED_CAST")
        if (result.isSuccess && result.getOrNull() == null)
            Result.failure(Error("Can not be null"))
        else result.map { it!!.blockhash } as Result<String> // safe cast, null case handled above
    }

fun Api.getRecentBlockhash(onComplete: ((Result<String>) -> Unit)) {
    CoroutineScope(dispatcher).launch {
        onComplete(getRecentBlockhash())
    }
}
