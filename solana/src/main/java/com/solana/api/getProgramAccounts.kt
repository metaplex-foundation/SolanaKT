package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.*
import com.solana.networking.AccountInfoWithPublicKey
import com.solana.networking.ProgramAccountsSerializer
import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*


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
                val filter = filters.toJsonArray()
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
                is DataSize -> addJsonObject {
                    put("dataSize", filter.dataSize)
                }
                is Filter -> addJsonObject {
                    putJsonObject("memcmp") {
                        put("offset", filter.memcmp.offset)
                        put("bytes", filter.memcmp.bytes)
                    }
                }
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

@Serializable
data class ProgramAccountSerialized<T>(
    val account: AccountInfo<T>,
    val pubkey: String
)

fun <T> Api.getProgramAccounts(
    serializer: KSerializer<T>,
    account: PublicKey,
    offset: Long,
    bytes: String,
    onComplete: (Result<List<ProgramAccountSerialized<T>>>) -> Unit
) {
    val filters: MutableList<Any> = ArrayList()
    filters.add(Filter(Memcmp(offset, bytes)))
    val programAccountConfig = ProgramAccountConfig(filters = filters)
    return getProgramAccounts(serializer, account, programAccountConfig, onComplete)
}

fun <T> Api.getProgramAccounts(
    serializer: KSerializer<T>,
    account: PublicKey,
    onComplete: (Result<List<ProgramAccountSerialized<T>>>) -> Unit
) {
    return getProgramAccounts(
        serializer,
        account,
        ProgramAccountConfig(RpcSendTransactionConfig.Encoding.base64),
        onComplete
    )
}

fun <T> Api.getProgramAccounts(
    serializer: KSerializer<T>,
    account: PublicKey,
    programAccountConfig: ProgramAccountConfig?,
    onComplete: (Result<List<ProgramAccountSerialized<T>>>) -> Unit
) {
    CoroutineScope(dispatcher).launch {
        val result = getProgramAccounts(
            serializer,
            account,
            programAccountConfig ?: ProgramAccountConfig()
        ).map { programAccount ->
            programAccount.map { ProgramAccountSerialized(it.account, it.publicKey) }
        }
        onComplete(result)
    }
}

fun <T> Api.getProgramAccounts(
    serializer: KSerializer<T>,
    account: PublicKey,
    memcmpList: List<Memcmp>,
    dataSize: Int,
    onComplete: (Result<List<ProgramAccountSerialized<T>>>) -> Unit
) {
    val filters: MutableList<Any> = ArrayList()
    memcmpList.forEach {
        filters.add(
            Filter(
                it
            )
        )
    }

    filters.add(DataSize(dataSize.toLong()))
    val programAccountConfig = ProgramAccountConfig(filters = filters)

    CoroutineScope(dispatcher).launch {
        val result = getProgramAccounts(
            serializer,
            account,
            programAccountConfig
        ).map { programAccount ->
            programAccount.map { ProgramAccountSerialized(it.account, it.publicKey) }
        }
        onComplete(result)
    }
}

fun <T> Api.getProgramAccounts(
    serializer: KSerializer<T>,
    account: PublicKey,
    memcmpList: List<Memcmp>,
    onComplete: (Result<List<ProgramAccountSerialized<T>>>) -> Unit
) {
    val filters: MutableList<Any> = ArrayList()
    memcmpList.forEach {
        filters.add(
            Filter(
                it
            )
        )
    }


    val programAccountConfig = ProgramAccountConfig(filters = filters)
    CoroutineScope(dispatcher).launch {
        val result = getProgramAccounts(
            serializer,
            account,
            programAccountConfig
        ).map { programAccount ->
            programAccount.map { ProgramAccountSerialized(it.account, it.publicKey) }
        }
        onComplete(result)
    }
}

suspend fun <A> Api.getProgramAccounts(
    serializer: KSerializer<A>,
    account: PublicKey,
    programAccountConfig: ProgramAccountConfig
): Result<List<AccountInfoWithPublicKey<A>>> =
    router.makeRequestResult(
        ProgramAccountRequest(
            account.toString(),
            programAccountConfig.encoding, programAccountConfig.filters,
            programAccountConfig.dataSlice, programAccountConfig.commitment
        ),
        ProgramAccountsSerializer(serializer)
    ).let { result ->
        @Suppress("UNCHECKED_CAST")
        if (result.isSuccess && result.getOrNull() == null) Result.success(listOf())
        else result as Result<List<AccountInfoWithPublicKey<A>>> // safe cast, null case handled above
    }