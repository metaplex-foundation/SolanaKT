package com.solana.rxsolana.actions

import com.solana.actions.Action
import com.solana.actions.sendSPLTokens
import com.solana.core.HotAccount
import com.solana.core.PublicKey
import io.reactivex.Single
import io.reactivex.disposables.Disposables

fun Action.sendSPLTokens(
    account: HotAccount,
    mintAddress: PublicKey,
    fromPublicKey: PublicKey,
    destinationAddress: PublicKey,
    allowUnfundedRecipient: Boolean = false,
    amount: Long

): Single<String> {
    return Single.create { emitter ->
        this.sendSPLTokens(
            mintAddress = mintAddress,
            fromPublicKey = fromPublicKey,
            destinationAddress = destinationAddress,
            amount = amount,
            allowUnfundedRecipient = allowUnfundedRecipient,
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