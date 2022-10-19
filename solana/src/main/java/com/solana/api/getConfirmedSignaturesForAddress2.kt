package com.solana.api

import com.solana.core.PublicKey
import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.*

class GetConfirmedSignaturesForAddress2Request(
    account: PublicKey,
    limit: Int? = null,
    before: String? = null,
    until: String? = null,
) : RpcRequestSerializable() {
    override val method: String = "getConfirmedSignaturesForAddress2"
    override val params = buildJsonArray {
        add(account.toString())
        addJsonObject {
            put("limit", limit?.toLong())
            put("before", before)
            put("until", until)
        }
    }
}

@Serializable
data class SignatureInformation(
    var err: JsonObject? = null,
    val memo: JsonObject? = null,
    val signature: String? = null,
    val confirmationStatus: String,
    val slot: Long,
    val blockTime: Long
)

internal fun GetConfirmedSignaturesForAddress2Serializer() =
    ListSerializer(SignatureInformation.serializer())

fun Api.getConfirmedSignaturesForAddress2(
    account: PublicKey,
    limit: Int? = null,
    before: String? = null,
    until: String? = null,
    onComplete: (Result<List<SignatureInformation>>) -> Unit
) {
    CoroutineScope(dispatcher).launch {
        onComplete(
            getConfirmedSignaturesForAddress2(
                account,
                limit,
                before,
                until
            )
        )
    }
}

suspend fun Api.getConfirmedSignaturesForAddress2(
    account: PublicKey,
    limit: Int? = null,
    before: String? = null,
    until: String? = null,
): Result<List<SignatureInformation>> =
    router.makeRequestResult(
        GetConfirmedSignaturesForAddress2Request(
            account,
            limit,
            before,
            until
        ), GetConfirmedSignaturesForAddress2Serializer()
    ).let { result ->
        @Suppress("UNCHECKED_CAST")
        if (result.isSuccess && result.getOrNull() == null) {
            Result.failure(Error("Can not be null"))
        } else {
            result as Result<List<SignatureInformation>>
        } // safe cast, null case handled above
    }
