package com.solana.rxsolana.api

import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.models.ConfirmedTransaction
import io.reactivex.Single
import io.reactivex.disposables.Disposables

public fun Api.getRecentBlockhash(): Single<String> {
    return Single.create { emitter ->
         this.getRecentBlockhash { result ->
             result.onSuccess {
                 emitter.onSuccess(it)
             }.onFailure {
                 emitter.onError(it)
             }
         }
        Disposables.empty()
    }
}

public fun Api.getBalance(account: PublicKey): Single<Long> {
    return Single.create { emitter ->
        this.getBalance(account) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

public fun Api.getConfirmedTransaction(signature: String): Single<ConfirmedTransaction> {
    return Single.create { emitter ->
        this.getConfirmedTransaction(signature,) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

public fun Api.sendTransaction(transaction: Transaction, signer: Account): Single<String> {
    return Single.create { emitter ->
        this.sendTransaction(transaction, signer) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}
