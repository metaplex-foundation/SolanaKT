package com.solana.models

data class RpcEpochConfig(
    val epoch: Long,
    val commitment: String?
)