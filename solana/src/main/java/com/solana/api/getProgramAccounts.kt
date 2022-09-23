package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.*
import com.solana.models.buffer.BufferInfo
import com.solana.networking.RpcRequestSerializable
import com.solana.vendor.borshj.Borsh
import com.solana.vendor.borshj.BorshCodable
import com.squareup.moshi.Types
import kotlinx.serialization.json.*
import java.lang.reflect.Type
import java.util.function.Consumer

class ProgramAccountRequest(
    account: String,
    encoding: RpcSendTransactionConfig.Encoding = RpcSendTransactionConfig.Encoding.base64,
    filters: List<Any>? = null,
    dataSlice: DataSlice? = null,
    commitment: String = "processed"
) : RpcRequestSerializable() {
    override val method = "getProgramAccounts"
    override val params = buildJsonArray {
        add(account)
        addJsonObject {
            put("encoding", encoding.getEncoding())
            put("commitment", commitment)

            dataSlice?.let {
                putJsonObject("dataSlice") {
                    put("length", dataSlice.length)
                    put("offset", dataSlice.offset)
                }
            }

            filters?.let {
                put("filters", filters.toJsonArray())
            }
        }
    }

    private fun List<Any>.toJsonArray(): JsonArray = buildJsonArray {
        this@toJsonArray.forEach { filter ->
            (filter as? Map<String, Any>)?.let { filterMap ->
                add(filterMap.toJsonObject())
            } ?: when (filter) {
                is Number -> add(filter)
                is String -> add(filter)
                is Boolean -> add(filter)
            }
        }
    }

    private fun Map<String, Any>.toJsonObject(): JsonObject = buildJsonObject {
        this@toJsonObject.forEach { (k, v) ->
            (v as? Map<String, Any>)?.let {
                put(k, v.toJsonObject())
            } ?: when (v) {
                is Number -> put(k, v)
                is String -> put(k, v)
                is Boolean -> put(k, v)
            }
        }
    }
}

fun <T: BorshCodable>Api.getProgramAccounts(
    account: PublicKey,
    offset: Long,
    bytes: String,
    decodeTo: Class<T>,
    onComplete: (Result<List<ProgramAccount<T>>>) -> Unit
){
    val filters: MutableList<Any> = ArrayList()
    filters.add(Filter(Memcmp(offset, bytes)))
    val programAccountConfig = ProgramAccountConfig(filters = filters)
    return getProgramAccounts(account, programAccountConfig, decodeTo, onComplete)
}

fun <T: BorshCodable> Api.getProgramAccounts(account: PublicKey,
                               decodeTo: Class<T>,
                               onComplete: (Result<List<ProgramAccount<T>>>) -> Unit
) {
    return getProgramAccounts(account, ProgramAccountConfig(RpcSendTransactionConfig.Encoding.base64), decodeTo, onComplete)
}

fun <T: BorshCodable> Api.getProgramAccounts(
    account: PublicKey,
    programAccountConfig: ProgramAccountConfig?,
    decodeTo: Class<T>,
    onComplete: (Result<List<ProgramAccount<T>>>) -> Unit
) {
    val params: MutableList<Any> = ArrayList()
    params.add(account.toString())
    if (programAccountConfig != null) {
        params.add(programAccountConfig)
    }
    val type = Types.newParameterizedType(
        List::class.java,
        Types.newParameterizedType(
            ProgramAccount::class.java,
            Type::class.java.cast(decodeTo)
        )
    )
    router.request<List<ProgramAccount<T>>>(
        "getProgramAccounts", params,
        type
    ){ result ->
        result.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}

fun <T :BorshCodable> Api.getProgramAccounts(
    account: PublicKey,
    memcmpList: List<Memcmp>,
    dataSize: Int,
    decodeTo: Class<T>,
    onComplete: (Result<List<ProgramAccount<T>>>) -> Unit
) {
    val params: MutableList<Any> = ArrayList()
    params.add(account.toString())
    val filters: MutableList<Any> = ArrayList()
    memcmpList.forEach(Consumer { memcmp: Memcmp ->
        filters.add(
            Filter(
                memcmp
            )
        )
    })
    filters.add(DataSize(dataSize.toLong()))
    val programAccountConfig = ProgramAccountConfig(filters = filters)
    params.add(programAccountConfig)

    val type = Types.newParameterizedType(
        List::class.java,
        Types.newParameterizedType(
            ProgramAccount::class.java,
            Type::class.java.cast(decodeTo)
        )
    )

    router.request<List<ProgramAccount<T>>>(
        "getProgramAccounts", params,
        type
    ) { result ->
        result.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}

fun <T :BorshCodable>Api.getProgramAccounts(account: PublicKey,
                                            memcmpList: List<Memcmp>,
                                            decodeTo: Class<T>,
                                            onComplete: (Result<List<ProgramAccount<T>>>) -> Unit
) {
    val params: MutableList<Any> = ArrayList()
    params.add(account.toString())
    val filters: MutableList<Any> = ArrayList()
    memcmpList.forEach(Consumer { memcmp: Memcmp ->
        filters.add(
            Filter(
                memcmp
            )
        )
    })

    val type = Types.newParameterizedType(
        List::class.java,
        Types.newParameterizedType(
            ProgramAccount::class.java,
            Type::class.java.cast(decodeTo)
        )
    )

    val programAccountConfig = ProgramAccountConfig(filters = filters)
    params.add(programAccountConfig)
    router.request<List<ProgramAccount<T>>>(
        "getProgramAccounts", params,
        type
    ){ result ->
        result.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}