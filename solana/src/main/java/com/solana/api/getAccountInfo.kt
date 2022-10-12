package com.solana.api

import com.solana.RPC_ACYNC_CALLBACK_INFO_DEPRECATION_MESSAGE
import com.solana.core.PublicKey
import com.solana.models.RpcSendTransactionConfig
import com.solana.models.buffer.Buffer
import com.solana.models.buffer.BufferInfo
import com.solana.networking.*
import com.solana.networking.serialization.serializers.base64.BorshAsBase64JsonArraySerializer
import com.solana.networking.serialization.serializers.legacy.BorshCodeableSerializer
import com.solana.networking.serialization.serializers.solana.AnchorAccountSerializer
import com.solana.networking.serialization.serializers.solana.SolanaResponseSerializer
import com.solana.vendor.ResultError
import com.solana.vendor.borshj.BorshCodable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

class AccountRequest(
    accountAddress: String,
    encoding: RpcSendTransactionConfig.Encoding = RpcSendTransactionConfig.Encoding.base64,
    commitment: String = "max",
    length: Int? = null,
    offset: Int? = length?.let { 0 }
) : RpcRequestSerializable() {
    override val method = "getAccountInfo"
    override val params = buildJsonArray {
        add(accountAddress)
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

@Serializable
data class AccountInfo<D>(val data: D?, val executable: Boolean,
                          val lamports: Long, val owner: String?, val rentEpoch: Long)

fun <D, T: BorshCodable> AccountInfo<D>.toBufferInfo() =
    BufferInfo(data?.let { Buffer(data as T) }, executable, lamports, owner, rentEpoch)

fun <A> SolanaAccountSerializer(serializer: KSerializer<A>) =
    AccountInfoSerializer(
        BorshAsBase64JsonArraySerializer(
            AnchorAccountSerializer(serializer.descriptor.serialName, serializer)
        )
    )

fun <D> AccountInfoSerializer(serializer: KSerializer<D>) =
    SolanaResponseSerializer(AccountInfo.serializer(serializer))

inline fun <reified A> SolanaAccountSerializer() =
    AccountInfoSerializer<A?>(BorshAsBase64JsonArraySerializer(AnchorAccountSerializer()))

inline fun <reified T> Api.getAccountInfo(
    serializer: KSerializer<T>,
    account: PublicKey,
    commitment: String = "max",
    encoding: RpcSendTransactionConfig.Encoding = RpcSendTransactionConfig.Encoding.base64,
    length: Int? = null,
    offset: Int? = length?.let { 0 },
    crossinline onComplete: ((Result<T>) -> Unit)
) {
    CoroutineScope(dispatcher).launch {
        onComplete(getAccountInfo(serializer, account, commitment, encoding, length, offset))
    }
}

suspend inline fun <reified A> Api.getAccountInfo(
    serializer: KSerializer<A>,
    account: PublicKey,
    commitment: String = "max",
    encoding: RpcSendTransactionConfig.Encoding = RpcSendTransactionConfig.Encoding.base64,
    length: Int? = null,
    offset: Int? = length?.let { 0 }
): Result<A> =
    router.makeRequestResult(
        AccountRequest(
            accountAddress = account.toBase58(),
            encoding = encoding,
            commitment = commitment,
            length = length,
            offset =  offset
        ),
        serializer
    ).let { result ->
        @Suppress("UNCHECKED_CAST")
        if (result.isSuccess && result.getOrNull() == null)
            Result.failure(Error("Account return Null"))
        else result as Result<A> // safe cast, null case handled above
    }


@Deprecated(RPC_ACYNC_CALLBACK_INFO_DEPRECATION_MESSAGE, ReplaceWith("getAccountInfo(serializer, account, commitment, length, offset, onComplete)"))
inline fun <reified T: BorshCodable> Api.getAccountInfo(
    account: PublicKey,
    decodeTo: Class<T>,
    crossinline onComplete: ((com.solana.vendor.Result<BufferInfo<T>, ResultError>) -> Unit)
) {
    return getAccountInfo(account, HashMap(), decodeTo, onComplete)
}

@Deprecated(RPC_ACYNC_CALLBACK_INFO_DEPRECATION_MESSAGE, ReplaceWith("getAccountInfo(serializer, account, commitment, encoding, length, offset, onComplete)"))
inline fun <reified T: BorshCodable> Api.getAccountInfo(
    account: PublicKey,
    additionalParams: Map<String, Any?>,
    decodeTo: Class<T>,
    crossinline onComplete: ((com.solana.vendor.Result<BufferInfo<T>, ResultError>) -> Unit)
) {

    val commitment = additionalParams["commitment"] ?: "max"
    val encoding = when (additionalParams["encoding"]) {
        "base64" -> RpcSendTransactionConfig.Encoding.base64
        "base58" -> RpcSendTransactionConfig.Encoding.base58
        else -> RpcSendTransactionConfig.Encoding.base64
    }

    var length: Int? = null
    var offset: Int? = null
    if (additionalParams.containsKey("dataSlice")) {
        length = (additionalParams["dataSlice"] as Map<String, Int>)["length"]
        offset = (additionalParams["dataSlice"] as Map<String, Int>)["offset"]
    }

    CoroutineScope(Dispatchers.IO).launch {
        getAccountInfo(
            serializer = BorshCodeableSerializer(decodeTo) as KSerializer<AccountInfo<T>>,
            account= account,
            commitment = commitment as String,
            encoding = encoding,
            length = length,
            offset = offset
        ).onSuccess {
            onComplete(com.solana.vendor.Result.success(it.toBufferInfo()))
        }.onFailure {
            onComplete(com.solana.vendor.Result.failure(ResultError(it)))
        }
    }
}

val nullValueError = ResultError("Account return Null")
