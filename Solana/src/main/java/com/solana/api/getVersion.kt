package com.solana.api

import com.solana.models.SolanaVersion

fun Api.getVersion(onComplete: ((Result<SolanaVersion>) -> Unit)) {
    router.call("getVersion", ArrayList(), SolanaVersion::class.java, onComplete)
}