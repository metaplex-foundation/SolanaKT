package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.RpcSendTransactionConfig
import com.solana.networking.MultipleAccountsSerializer
import com.solana.networking.RpcRequest
import com.solana.networking.makeRequestResult
import com.solana.vendor.ResultError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.*

class MultipleAccountsRequest(
    accounts: List<String>,
    encoding: RpcSendTransactionConfig.Encoding = RpcSendTransactionConfig.Encoding.base64,
    commitment: String = "max",
    length: Int? = null,
    offset: Int? = length?.let { 0 }
) : RpcRequest() {
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

fun <A> Api.getMultipleAccounts(
    serializer: KSerializer<A>,
    accounts: List<PublicKey>,
    onComplete: ((Result<List<AccountInfo<A>?>>) -> Unit)
) {
    CoroutineScope(dispatcher).launch {
        getMultipleAccountsInfo(
            serializer= serializer,
            accounts
        ).onSuccess {
            val buffers = it.map { account ->
                account
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
