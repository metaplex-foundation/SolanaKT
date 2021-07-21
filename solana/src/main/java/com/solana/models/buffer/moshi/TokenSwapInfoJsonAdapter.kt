package com.solana.models.buffer.moshi

import com.solana.models.buffer.Buffer
import com.solana.models.buffer.TokenSwapInfo
import com.solana.vendor.borshj.Borsh
import com.squareup.moshi.FromJson

class TokenSwapInfoJsonAdapter(val borsh: Borsh) {
    @FromJson
    fun fromJson(rawData: Any): Buffer<TokenSwapInfo> {
        return Buffer.create(borsh, rawData, TokenSwapInfo::class.java)
    }
}