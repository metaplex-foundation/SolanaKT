package com.solana.rxsolana.actions

import com.solana.actions.Action
import com.solana.actions.getTokenWallets
import com.solana.core.PublicKey
import com.solana.models.Wallet
import io.reactivex.Single
import io.reactivex.disposables.Disposables

fun Action.getTokenWallets(
    account: PublicKey,
): Single<List<Wallet>> {
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
