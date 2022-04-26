package com.solana.api

import com.solana.models.SignatureStatus
import com.solana.models.SignatureStatusRequestConfiguration
import java.lang.RuntimeException

fun Api.getSignatureStatuses(signatures: List<String>, configs: SignatureStatusRequestConfiguration? = SignatureStatusRequestConfiguration(), onComplete: ((Result<SignatureStatus>) -> Unit)) {
    val params: MutableList<Any> = ArrayList()
    params.add(signatures)
    if (configs != null) {
        params.add(configs)
    }
    return router.request<SignatureStatus>("getSignatureStatuses", params, SignatureStatus::class.javaObjectType){ result ->
        result.onSuccess { signatureStatuses ->
            onComplete(Result.success(signatureStatuses))
            return@request
        }.onFailure {
            onComplete(Result.failure(RuntimeException(it)))
            return@request
        }
    }
}