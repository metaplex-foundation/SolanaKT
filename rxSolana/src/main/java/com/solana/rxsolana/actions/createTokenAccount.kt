package com.solana.rxsolana.actions

import com.solana.actions.Action
import com.solana.actions.createTokenAccount
import com.solana.core.HotAccount
import com.solana.core.PublicKey
import io.reactivex.Single
import io.reactivex.disposables.Disposables

fun Action.createTokenAccount(
    account: HotAccount,
    mintAddress: PublicKey
) : Single<Pair<String, PublicKey>> {
    return Single.create { emitter ->
        this.createTokenAccount(account, mintAddress) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}