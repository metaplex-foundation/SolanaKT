package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.RPC
import com.solana.models.RpcSendTransactionConfig
import com.solana.models.buffer.BufferInfo
import com.solana.networking.RpcRequestSerializable
import com.squareup.moshi.Types
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

fun <T> Api.getMultipleAccounts(
    accounts: List<PublicKey>,
    decodeTo: Class<T>,
    onComplete: ((Result<List<BufferInfo<T>?>>) -> Unit)
) {

    val publickeys = accounts.map { it.toBase58() }
    val params: MutableList<Any> = ArrayList()
    val parameterMap: MutableMap<String, Any?> = HashMap()

    parameterMap["encoding"] = "base64"
    params.add(publickeys)
    params.add(parameterMap)

    val type = Types.newParameterizedType(
        RPC::class.java,
        Types.newParameterizedType(
            List::class.java,
            Types.newParameterizedType(
                BufferInfo::class.java,
                Type::class.java.cast(decodeTo)
            )
        )
    )

    router.request<RPC<List<BufferInfo<T>>>>("getMultipleAccounts", params, type) { result ->
        result
            .map {
                it.value!!
            }
            .onSuccess {
                onComplete(Result.success(it))
            }
            .onFailure {
                onComplete(Result.failure(it))
            }
    }
}