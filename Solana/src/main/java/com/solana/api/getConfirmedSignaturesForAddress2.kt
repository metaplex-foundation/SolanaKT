package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.ConfirmedSignFAddr2
import com.solana.models.SignatureInformation

fun Api.getConfirmedSignaturesForAddress2(
    account: PublicKey,
    limit: Int? = null,
    before: String?  = null,
    until: String? = null,
    onComplete: (Result<List<SignatureInformation>>) -> Unit
) {
    val params: MutableList<Any> = ArrayList()
    params.add(account.toString())
    params.add( ConfirmedSignFAddr2(limit = limit?.toLong(), before = before, until = until) )

    router.request(
        "getConfirmedSignaturesForAddress2", params,
        List::class.java
    ) { result ->
        result.map {
            it.filterNotNull()
        }.map{
            it.map { item -> item as Map<String, Any> }
        }.map{
            val list: MutableList<SignatureInformation> = ArrayList()
            for (item in it) {
                list.add(SignatureInformation(item))
            }
            list
        }.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}
