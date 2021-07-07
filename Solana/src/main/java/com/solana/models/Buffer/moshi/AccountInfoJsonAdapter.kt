package com.solana.models.Buffer.moshi

import com.solana.models.Buffer.AccountInfo
import com.solana.models.Buffer.Buffer
import com.solana.vendor.borshj.Borsh
import com.squareup.moshi.FromJson

class AccountInfoJsonAdapter(val borsh: Borsh) {
    @FromJson
    fun fromJson(rawData: Any): Buffer<AccountInfo> {
        return Buffer.create(borsh, rawData, AccountInfo::class.java)
    }
}