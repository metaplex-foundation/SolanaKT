package com.solana.models.Buffer.moshi

import com.solana.models.Buffer.AccountInfo
import com.solana.models.Buffer.Buffer2
import com.solana.vendor.borshj.Borsh
import com.squareup.moshi.FromJson

class AccountInfoJsonAdapter(val borsh: Borsh) {
    @FromJson
    fun fromJson(rawData: Any): Buffer2<AccountInfo> {
        return Buffer2.create(borsh, rawData, AccountInfo::class.java)
    }
}