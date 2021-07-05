package com.solana.models

import com.solana.core.PublicKey
import com.solana.core.PublicKeyRule
import com.solana.vendor.borshj.BorshCodable
import com.solana.vendor.borshj.BorshInput
import com.solana.vendor.borshj.BorshOutput
import com.solana.vendor.borshj.BorshRule

data class MetaplexMeta(
    val key: Byte,
    val update_authority: PublicKey,
    val mint: PublicKey,
    val data: MetaplexData
): BorshCodable

class MetaplexMetaRule(
    override val clazz: Class<MetaplexMeta> = MetaplexMeta::class.java
): BorshRule<MetaplexMeta> {

    override fun read(input: BorshInput): MetaplexMeta {
        return MetaplexMeta(
            input.readU8(),
            PublicKeyRule().read(input),
            PublicKeyRule().read(input),
            MetaplexDataRule().read(input)
        )
    }

    override fun <Self> write(obj: Any, output: BorshOutput<Self>): Self {
        TODO("Not yet implemented")
    }

}

data class MetaplexData(
    val name: String,
    val symbol: String,
    val uri: String
): BorshCodable

class MetaplexDataRule(
    override val clazz: Class<MetaplexData> = MetaplexData::class.java
): BorshRule<MetaplexData> {

    override fun read(input: BorshInput): MetaplexData {
        val name = input.readString()
        val symbol = input.readString()
        val uri = input.readString()

        return MetaplexData(
            name,
            symbol,
            uri
        )
    }

    override fun <Self> write(obj: Any, output: BorshOutput<Self>): Self {
        TODO("Not yet implemented")
    }

}