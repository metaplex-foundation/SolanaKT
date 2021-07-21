package com.solana.rxsolana.actions

import com.solana.actions.Action
import com.solana.actions.SPLTokenDestinationAddress
import com.solana.actions.findSPLTokenDestinationAddress
import com.solana.core.PublicKey
import io.reactivex.Single
import io.reactivex.disposables.Disposables

fun Action.findSPLTokenDestinationAddress(
    mintAddress: PublicKey,
    destinationAddress: PublicKey,
) : Single<SPLTokenDestinationAddress> {
    return Single.create { emitter ->
        this.findSPLTokenDestinationAddress(mintAddress, destinationAddress) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}