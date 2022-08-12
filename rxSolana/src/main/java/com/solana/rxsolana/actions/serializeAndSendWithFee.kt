package com.solana.rxsolana.actions

import com.solana.actions.Action
import com.solana.actions.serializeAndSendWithFee
import com.solana.core.HotAccount
import com.solana.core.Transaction
import io.reactivex.Single
import io.reactivex.disposables.Disposables

fun Action.serializeAndSendWithFee(
    transaction: Transaction,
    signers: List<HotAccount>,
    recentBlockHash: String? = null
): Single<String> {
    return Single.create { emitter ->
        this.serializeAndSendWithFee(transaction, signers, recentBlockHash) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}
