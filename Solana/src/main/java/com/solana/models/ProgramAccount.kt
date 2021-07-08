package com.solana.models

import com.solana.models.buffer.*
import com.solana.vendor.borshj.BorshCodable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ProgramAccount<T: BorshCodable>(
    @Json(name = "account") val account: BufferInfo<T>,
    @Json(name = "pubkey")  val pubkey: String
)