package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.*
import com.solana.models.Buffer.BufferLayout
import java.util.function.Consumer

fun <T>Api.getProgramAccounts(
    account: PublicKey,
    offset: Long,
    bytes: String,
    decodeTo: Class<T>,
    bufferLayout: BufferLayout<T>,
    onComplete: (Result<List<ProgramAccount<T>>>) -> Unit
){
    val filters: MutableList<Any> = ArrayList()
    filters.add(Filter(Memcmp(offset, bytes)))
    val programAccountConfig = ProgramAccountConfig(filters = filters)
    return getProgramAccounts(account, programAccountConfig, decodeTo, bufferLayout, onComplete)
}

fun <T> Api.getProgramAccounts(account: PublicKey,
                               decodeTo: Class<T>,
                               bufferLayout: BufferLayout<T>,
                               onComplete: (Result<List<ProgramAccount<T>>>) -> Unit
) {
    return getProgramAccounts(account, ProgramAccountConfig(RpcSendTransactionConfig.Encoding.base64), decodeTo, bufferLayout, onComplete)
}

private fun <T> Api.getProgramAccounts(
    account: PublicKey,
    programAccountConfig: ProgramAccountConfig?,
    decodeTo: Class<T>,
    bufferLayout: BufferLayout<T>,
    onComplete: (Result<List<ProgramAccount<T>>>) -> Unit
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
            val result: MutableList<ProgramAccount<T>> = ArrayList()
            for (item in it) {
                result.add(ProgramAccount(item, decodeTo, bufferLayout))
            }
            result
        }.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}

fun <T> Api.getProgramAccounts(
    account: PublicKey,
    memcmpList: List<Memcmp>,
    dataSize: Int,
    decodeTo: Class<T>,
    bufferLayout: BufferLayout<T>,
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
    router.call(
        "getProgramAccounts", params,
        List::class.java
    ) { result ->
        result.map{
            it.map { item -> item as Map<String, Any> }
        }.map{
            val result: MutableList<ProgramAccount<T>> = ArrayList()
            for (item in it) {
                result.add(ProgramAccount(item, decodeTo, bufferLayout))
            }
            result
        }.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}

fun <T>Api.getProgramAccounts(account: PublicKey,
                       memcmpList: List<Memcmp>, decodeTo: Class<T>, bufferLayout: BufferLayout<T>,
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
    val programAccountConfig = ProgramAccountConfig(filters = filters)
    params.add(programAccountConfig)
    router.call(
        "getProgramAccounts", params,
        List::class.java
    ){ result ->
        result.map{
            it.map { item -> item as Map<String, Any> }
        }.map{
            val result: MutableList<ProgramAccount<T>> = ArrayList()
            for (item in it) {
                result.add(ProgramAccount(item, decodeTo, bufferLayout))
            }
            result
        }.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}