package com.solana.api

import com.solana.models.SolanaVersion

fun Api.getVersion(onComplete: ((Result<SolanaVersion>) -> Unit)) {
    router.request("getVersion", ArrayList(), SolanaVersion::class.java, onComplete)
}