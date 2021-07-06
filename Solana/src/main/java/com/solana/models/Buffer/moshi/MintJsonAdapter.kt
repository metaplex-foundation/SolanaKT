package com.solana.models.Buffer.moshi

import com.solana.models.Buffer.Buffer2
import com.solana.models.Buffer.Mint
import com.solana.vendor.borshj.Borsh
import com.squareup.moshi.FromJson

class MintJsonAdapter(val borsh: Borsh) {
    @FromJson
    fun fromJson(rawData: Any): Buffer2<Mint> {
        return Buffer2.create(borsh, rawData, Mint::class.java)
    }
}