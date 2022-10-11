package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.RPC
import com.solana.models.TokenResultObjects
import com.solana.models.buffer.BufferInfo
import com.solana.networking.RpcRequestSerializable
import com.solana.networking.SolanaResponseSerializer
import com.solana.networking.makeRequestResult
import com.squareup.moshi.Types
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import java.lang.reflect.Type

class GetTokenSupplyRequest(tokenMint: PublicKey) : RpcRequestSerializable() {
    override val method: String = "getTokenSupply"
    override val params = buildJsonArray {
        add(tokenMint.toString())
    }
}

internal fun GetTokenSupplySerializer() = SolanaResponseSerializer(TokenAmountInfoResponse.serializer())

suspend fun Api.getTokenSupply(tokenMint: PublicKey): Result<TokenAmountInfoResponse> =
    router.makeRequestResult(GetTokenSupplyRequest(tokenMint), GetTokenSupplySerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<TokenAmountInfoResponse> // safe cast, null case handled above
        }

fun Api.getTokenSupply(tokenMint: PublicKey, onComplete: (Result<TokenAmountInfoResponse>) -> Unit) {
    CoroutineScope(dispatcher).launch {
        onComplete(getTokenSupply(tokenMint))
    }
}