package com.solana.models

import com.solana.core.PublicKey
import com.solana.vendor.borshj.BorshCodable
import com.solana.vendor.borshj.PropertyOrdinal

data class MetaplexMeta(
    @PropertyOrdinal(order = 0) val key: Byte,
    @PropertyOrdinal(order = 1) val update_authority: PublicKey,
    @PropertyOrdinal(order = 2) val mint: PublicKey,
    @PropertyOrdinal(order = 3) val data: MetaplexData
): BorshCodable

data class MetaplexData(
    @PropertyOrdinal(order = 0) val name: String,
    @PropertyOrdinal(order = 0) val symbol: String,
    @PropertyOrdinal(order = 0) val uri: String
): BorshCodable