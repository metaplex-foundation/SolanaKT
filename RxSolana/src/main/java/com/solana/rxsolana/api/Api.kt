package com.solana.rxsolana.api

import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.models.*
import io.reactivex.Single
import io.reactivex.disposables.Disposables

fun Api.getRecentBlockhash(): Single<String> {
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

fun Api.getBalance(account: PublicKey): Single<Long> {
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

fun Api.getConfirmedTransaction(signature: String): Single<ConfirmedTransaction> {
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

fun Api.sendTransaction(transaction: Transaction, signer: Account): Single<String> {
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

fun Api.getVoteAccounts(): Single<VoteAccounts> {
    return Single.create { emitter ->
        this.getVoteAccounts() { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getStakeActivation(publicKey: PublicKey): Single<StakeActivation> {
    return Single.create { emitter ->
        this.getStakeActivation(publicKey) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}
fun Api.getStakeActivation(publicKey: PublicKey, epoch: Long): Single<StakeActivation> {
    return Single.create { emitter ->
        this.getStakeActivation(publicKey, epoch) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getAccountInfo(publicKey: PublicKey): Single<AccountInfo> {
    return Single.create { emitter ->
        this.getAccountInfo(publicKey) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.requestAirdrop(publicKey: PublicKey, lamports: Long): Single<String> {
    return Single.create { emitter ->
        this.requestAirdrop(publicKey, lamports) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getMinimumBalanceForRentExemption(dataLength: Long): Single<Long> {
    return Single.create { emitter ->
        this.getMinimumBalanceForRentExemption(dataLength) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getBlockTime(block: Long): Single<Long> {
    return Single.create { emitter ->
        this.getBlockTime(block) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getBlockHeight(): Single<Long> {
    return Single.create { emitter ->
        this.getBlockHeight { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.minimumLedgerSlot(): Single<Long> {
    return Single.create { emitter ->
        this.minimumLedgerSlot { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getVersion(): Single<SolanaVersion> {
    return Single.create { emitter ->
        this.getVersion { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}
fun Api.getFeeCalculatorForBlockhash(blockhash: String): Single<FeeCalculatorInfo> {
    return Single.create { emitter ->
        this.getFeeCalculatorForBlockhash(blockhash) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getBlockCommitment(block: Long): Single<BlockCommitment> {
    return Single.create { emitter ->
        this.getBlockCommitment(block) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getFeeRateGovernor(): Single<FeeRateGovernorInfo> {
    return Single.create { emitter ->
        this.getFeeRateGovernor { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getFees(): Single<FeesInfo> {
    return Single.create { emitter ->
        this.getFees { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getTransactionCount(): Single<Long> {
    return Single.create { emitter ->
        this.getTransactionCount { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getMaxRetransmitSlot(): Single<Long> {
    return Single.create { emitter ->
        this.getMaxRetransmitSlot { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getSupply(): Single<Supply> {
    return Single.create { emitter ->
        this.getSupply { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getFirstAvailableBlock(): Single<Long> {
    return Single.create { emitter ->
        this.getFirstAvailableBlock { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getGenesisHash(): Single<String> {
    return Single.create { emitter ->
        this.getGenesisHash { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getBlock(slot: Int): Single<Block> {
    return Single.create { emitter ->
        this.getBlock(slot) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getEpochInfo(): Single<EpochInfo> {
    return Single.create { emitter ->
        this.getEpochInfo { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getEpochSchedule(): Single<EpochSchedule> {
    return Single.create { emitter ->
        this.getEpochSchedule { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getConfirmedBlock(slot: Int): Single<ConfirmedBlock> {
    return Single.create { emitter ->
        this.getConfirmedBlock(slot) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getSnapshotSlot(): Single<Long> {
    return Single.create { emitter ->
        this.getSnapshotSlot { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getMaxShredInsertSlot(): Single<Long> {
    return Single.create { emitter ->
        this.getMaxShredInsertSlot { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getSlot(): Single<Long> {
    return Single.create { emitter ->
        this.getSlot { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getConfirmedBlocks(start: Int, end: Int): Single<List<Double>> {
    return Single.create { emitter ->
        this.getConfirmedBlocks(start,end) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getSplTokenAccountInfo(account: PublicKey): Single<SplTokenAccountInfo> {
    return Single.create { emitter ->
        this.getSplTokenAccountInfo(account) { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}

fun Api.getSlotLeader(): Single<PublicKey> {
    return Single.create { emitter ->
        this.getSlotLeader() { result ->
            result.onSuccess {
                emitter.onSuccess(it)
            }.onFailure {
                emitter.onError(it)
            }
        }
        Disposables.empty()
    }
}