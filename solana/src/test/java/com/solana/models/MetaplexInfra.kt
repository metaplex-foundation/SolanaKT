package com.solana.models

import com.solana.core.PublicKey
import com.solana.networking.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import kotlinx.serialization.Serializable

@Serializable
data class MetaplexMeta(
    val key: Byte,
    @Serializable(with = PublicKeyAs32ByteSerializer::class) val update_authority: PublicKey,
    @Serializable(with = PublicKeyAs32ByteSerializer::class) val mint: PublicKey,
    val data: MetaplexData
)

@Serializable
data class MetaplexData(
    val name: String,
    val symbol: String,
    val uri: String
)