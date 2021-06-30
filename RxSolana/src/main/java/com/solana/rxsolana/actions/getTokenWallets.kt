package com.solana.rxsolana.actions

import com.solana.actions.Action
import com.solana.actions.getTokenWallets
import com.solana.core.PublicKey
import io.reactivex.Single
import io.reactivex.disposables.Disposables

fun Action.getTokenWallets(
    account: PublicKey,
): Single<Any> {
    return Single.create { emitter ->
        this.getTokenWallets(account ) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}
