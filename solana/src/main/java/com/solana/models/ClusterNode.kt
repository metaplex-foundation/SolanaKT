package com.solana.models

import com.solana.core.PublicKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ClusterNode(
    var pubkey: PublicKey,
    var gossip: String?,
    var tpu: String?,
    var rpc: String?,
    var version: String?
)
