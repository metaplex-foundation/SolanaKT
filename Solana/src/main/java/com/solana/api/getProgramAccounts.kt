package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.*
import java.util.function.Consumer

fun Api.getProgramAccounts(
    account: PublicKey,
    offset: Long,
    bytes: String,
    onComplete: (Result<List<ProgramAccount>>) -> Unit
){
    val filters: MutableList<Any> = ArrayList()
    filters.add(Filter(Memcmp(offset, bytes)))
    val programAccountConfig = ProgramAccountConfig(filters = filters)
    return getProgramAccounts(account, programAccountConfig, onComplete)
}

fun Api.getProgramAccounts(account: PublicKey,
                       onComplete: (Result<List<ProgramAccount>>) -> Unit
) {
    return getProgramAccounts(account, ProgramAccountConfig(RpcSendTransactionConfig.Encoding.base64), onComplete)
}

private fun Api.getProgramAccounts(
    account: PublicKey,
    programAccountConfig: ProgramAccountConfig?,
    onComplete: (Result<List<ProgramAccount>>) -> Unit
) {
    val params: MutableList<Any> = ArrayList()
    params.add(account.toString())
    if (programAccountConfig != null) {
        params.add(programAccountConfig)
    }
    router.call(
        "getProgramAccounts", params,
        List::class.java
    ){ result ->
        result.map{
            it.map { item -> item as Map<String, Any> }
        }.map{
            val result: MutableList<ProgramAccount> = ArrayList()
            for (item in it) {
                result.add(ProgramAccount(item))
            }
            result
        }.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}

fun Api.getProgramAccounts(
    account: PublicKey,
    memcmpList: List<Memcmp>,
    dataSize: Int,
    onComplete: (Result<List<ProgramAccount>>) -> Unit
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
    router.call(
        "getProgramAccounts", params,
        List::class.java
    ) { result ->
        result.map{
            it.map { item -> item as Map<String, Any> }
        }.map{
            val result: MutableList<ProgramAccount> = ArrayList()
            for (item in it) {
                result.add(ProgramAccount(item))
            }
            result
        }.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}

fun Api.getProgramAccounts(account: PublicKey,
                       memcmpList: List<Memcmp>,
                       onComplete: (Result<List<ProgramAccount>>) -> Unit
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
    val programAccountConfig = ProgramAccountConfig(filters = filters)
    params.add(programAccountConfig)
    router.call(
        "getProgramAccounts", params,
        List::class.java
    ){ result ->
        result.map{
            it.map { item -> item as Map<String, Any> }
        }.map{
            val result: MutableList<ProgramAccount> = ArrayList()
            for (item in it) {
                result.add(ProgramAccount(item))
            }
            result
        }.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}