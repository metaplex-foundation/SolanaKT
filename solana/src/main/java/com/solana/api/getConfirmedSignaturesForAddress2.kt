package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.ConfirmedSignFAddr2
import com.solana.models.SignatureInformation
import com.squareup.moshi.Types

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

    router.request<List<SignatureInformation>>(
        "getConfirmedSignaturesForAddress2", params,
        Types.newParameterizedType(List::class.java, SignatureInformation::class.java)
    ) { result ->
        result.onSuccess {
            onComplete(Result.success(it))
        }.onFailure {
            onComplete(Result.failure(it))
        }
    }
}
