package com.solana.rxsolana.actions

import com.solana.actions.Action
import com.solana.actions.sendSPLTokens
import com.solana.core.Account
import com.solana.core.PublicKey
import io.reactivex.Single
import io.reactivex.disposables.Disposables

fun Action.sendSPLTokens(
    account: Account,
    mintAddress: PublicKey,
    fromPublicKey: PublicKey,
    destinationAddress: PublicKey,
    amount: Long
): Single<String> {
    return Single.create { emitter ->
        this.sendSPLTokens(
            mintAddress = mintAddress,
            fromPublicKey = fromPublicKey,
            destinationAddress = destinationAddress,
            amount = amount,
            account = account
        ) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}