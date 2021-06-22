package com.solana.rxsolana.api

import com.solana.models.RecentBlockhash
import io.reactivex.Single
import io.reactivex.disposables.Disposables

public fun Api.getRecentBlockhash(): Single<RecentBlockhash> {
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