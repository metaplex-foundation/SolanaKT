package com.solana.models.Buffer.moshi

import com.solana.models.Buffer.Buffer2
import com.solana.models.Buffer.TokenSwapInfo
import com.solana.vendor.borshj.Borsh
import com.squareup.moshi.FromJson

class TokenSwapInfoJsonAdapter(val borsh: Borsh) {
    @FromJson
    fun fromJson(rawData: Any): Buffer2<TokenSwapInfo> {
        return Buffer2.create(borsh, rawData, TokenSwapInfo::class.java)
    }
}