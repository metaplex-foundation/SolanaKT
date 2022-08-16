package com.solana.rxsolana.actions

import com.solana.actions.Action
import com.solana.core.PublicKey
import com.solana.models.buffer.Mint
import com.solana.programs.TokenProgram
import io.reactivex.Single
import io.reactivex.disposables.Disposables
import com.solana.actions.getMintData
import com.solana.actions.getMultipleMintDatas

fun Action.getMintData(
    mintAddress: PublicKey,
    programId: PublicKey = TokenProgram.PROGRAM_ID,
): Single<Mint> {
    return Single.create { emitter ->
        this.getMintData(mintAddress, programId) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Action.getMultipleMintDatas(
    mintAddresses: List<PublicKey>,
    programId: PublicKey = TokenProgram.PROGRAM_ID,
): Single<Map<PublicKey, Mint>> {
    return Single.create { emitter ->
        this.getMultipleMintDatas(mintAddresses, programId) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}
