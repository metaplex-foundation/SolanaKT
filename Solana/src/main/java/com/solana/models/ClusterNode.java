package com.solana.models;

import com.solana.core.PublicKey;
import com.squareup.moshi.Json;

import java.util.AbstractMap;

public class ClusterNode {

    // Constructor for deserializing into List
    @SuppressWarnings({ "rawtypes" })
    public ClusterNode(AbstractMap pa) {
        this.pubkey = new PublicKey((String) pa.get("pubkey"));
        this.gossip = (String) pa.get("gossip");
        this.tpu = (String) pa.get("tpu");
        this.rpc = (String) pa.get("rpc");
        this.version = (String) pa.get("version");
    }

    @Json(name = "pubkey")
    private PublicKey pubkey;

    @Json(name = "gossip")
    private String gossip;

    @Json(name = "tpu")
    private String tpu;

    @Json(name = "rpc")
    private String rpc;

    @Json(name = "version")
    private String version;
}
