package com.solana.models

import com.solana.core.PublicKey
import com.squareup.moshi.Json

class ClusterNode(pa: Map<*, *>) {
    @Json(name = "pubkey")
    var pubkey: PublicKey

    @Json(name = "gossip")
    var gossip: String?

    @Json(name = "tpu")
    var tpu: String?

    @Json(name = "rpc")
    var rpc: String?

    @Json(name = "version")
    var version: String?

    // Constructor for deserializing into List
    init {
        pubkey = PublicKey(pa["pubkey"] as String?)
        gossip = pa["gossip"] as String?
        tpu = pa["tpu"] as String?
        rpc = pa["rpc"] as String?
        version = pa["version"] as String?
    }
}