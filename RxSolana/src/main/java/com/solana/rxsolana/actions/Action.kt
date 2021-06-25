package com.solana.rxsolana.actions

import com.solana.actions.Action
import com.solana.core.Account
import com.solana.core.PublicKey
import io.reactivex.Single
import io.reactivex.disposables.Disposables

fun Action.sendSOL(
    account: Account,
    destination: PublicKey,
    amount: Long
): Single<String> {
    return Single.create { emitter ->
        this.sendSOL(account, destination, amount) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}
