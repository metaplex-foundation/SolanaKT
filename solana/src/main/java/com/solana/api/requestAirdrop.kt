package com.solana.api

import com.solana.core.PublicKey
import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray

class RequestAirdropSlotRequest(address: PublicKey, lamports: Long) : RpcRequestSerializable() {
    override val method: String = "requestAirdrop"
    override val params = buildJsonArray {
        add(address.toBase58())
        add(lamports)
    }
}

internal fun RequestAirdropSerializer() = String.serializer()

suspend fun Api.requestAirdrop(address: PublicKey, lamports: Long): Result<String> =
    router.makeRequestResult(RequestAirdropSlotRequest(address, lamports), RequestAirdropSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<String> // safe cast, null case handled above
        }

fun Api.requestAirdrop(address: PublicKey, lamports: Long, onComplete: ((Result<String>) -> Unit)) {
    CoroutineScope(dispatcher).launch {
        onComplete(requestAirdrop(address, lamports))
    }
}