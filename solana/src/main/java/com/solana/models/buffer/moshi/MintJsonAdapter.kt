package com.solana.models.buffer.moshi

import com.solana.models.buffer.Buffer
import com.solana.models.buffer.Mint
import com.solana.vendor.borshj.Borsh
import com.squareup.moshi.FromJson

class MintJsonAdapter(val borsh: Borsh) {
    @FromJson
    fun fromJson(rawData: Any): Buffer<Mint> {
        return Buffer.create(borsh, rawData, Mint::class.java)
    }
}