package com.solana.models.Buffer.moshi

import com.solana.models.Buffer.Buffer
import com.solana.models.Buffer.Mint
import com.solana.vendor.borshj.Borsh
import com.squareup.moshi.FromJson

class MintJsonAdapter(val borsh: Borsh) {
    @FromJson
    fun fromJson(rawData: Any): Buffer<Mint> {
        return Buffer.create(borsh, rawData, Mint::class.java)
    }
}