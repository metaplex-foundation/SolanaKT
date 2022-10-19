package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.RPC
import com.solana.models.RpcSendTransactionConfig
import com.solana.models.buffer.BufferInfo
import com.solana.networking.MultipleAccountsSerializer
import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import com.solana.networking.serialization.serializers.legacy.BorshCodeableSerializer
import com.solana.vendor.ResultError
import com.solana.vendor.borshj.BorshCodable
import com.squareup.moshi.Types
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.*
import java.lang.reflect.Type

class MultipleAccountsRequest(
    accounts: List<String>,
    encoding: RpcSendTransactionConfig.Encoding = RpcSendTransactionConfig.Encoding.base64,
    commitment: String = "max",
    length: Int? = null,
    offset: Int? = length?.let { 0 }
) : RpcRequestSerializable() {
    override val method = "getMultipleAccounts"
    override val params = buildJsonArray {
        addJsonArray {
            accounts.forEach {
                add(it)
            }
        }
        addJsonObject {
            put("encoding", encoding.getEncoding())
            put("commitment", commitment)
            length?.let {
                putJsonObject("dataSlice") {
                    put("length", length)
                    put("offset", offset)
                }
            }
        }
    }
}

fun <T: BorshCodable> Api.getMultipleAccounts(
    accounts: List<PublicKey>,
    decodeTo: Class<T>,
    onComplete: ((Result<List<BufferInfo<T>?>>) -> Unit)
) {
    CoroutineScope(dispatcher).launch {
        getMultipleAccountsInfo(
            serializer= BorshCodeableSerializer(decodeTo),
            accounts
        ).onSuccess {
            val buffers: List<BufferInfo<T>?> = it.map { account ->
                account?.toBufferInfo()
            }
            onComplete(Result.success(buffers))
        }.onFailure {
            onComplete(Result.failure(ResultError(it)))
        }
    }
}

suspend fun <A> Api.getMultipleAccountsInfo(
    serializer: KSerializer<A>,
    accounts: List<PublicKey>,
    encoding: RpcSendTransactionConfig.Encoding = RpcSendTransactionConfig.Encoding.base64,
    commitment: String = "max",
    length: Int? = null,
    offset: Int? = length?.let { 0 }
): Result<List<AccountInfo<A>?>> =
    router.makeRequestResult(
        MultipleAccountsRequest(
            accounts = accounts.map { it.toBase58() },
            encoding = encoding,
            commitment = commitment,
            length = length,
            offset = offset
        ),
        MultipleAccountsSerializer(serializer)
    ).let { result ->
        @Suppress("UNCHECKED_CAST")
        if (result.isSuccess && result.getOrNull() == null) Result.success(listOf())
        else result as Result<List<AccountInfo<A>?>> // safe cast, null case handled above
    }
