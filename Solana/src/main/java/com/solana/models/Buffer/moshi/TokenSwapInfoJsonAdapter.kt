package com.solana.models.Buffer.moshi

import com.solana.models.Buffer.Buffer
import com.solana.models.Buffer.TokenSwapInfo
import com.solana.vendor.borshj.Borsh
import com.squareup.moshi.FromJson

class TokenSwapInfoJsonAdapter(val borsh: Borsh) {
    @FromJson
    fun fromJson(rawData: Any): Buffer<TokenSwapInfo> {
        return Buffer.create(borsh, rawData, TokenSwapInfo::class.java)
    }
}