package com.solana.models.buffer.moshi

import com.solana.models.buffer.AccountInfo
import com.solana.models.buffer.Buffer
import com.solana.vendor.borshj.Borsh
import com.squareup.moshi.FromJson

class AccountInfoJsonAdapter(val borsh: Borsh) {
    @FromJson
    fun fromJson(rawData: Any): Buffer<AccountInfo> {
        return Buffer.create(borsh, rawData, AccountInfo::class.java)
    }
}