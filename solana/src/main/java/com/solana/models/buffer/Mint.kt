package com.solana.models.buffer

import com.solana.core.PublicKey
import com.solana.networking.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import kotlinx.serialization.Serializable

@Serializable
data class Mint(
    val mintAuthorityOption: Int,
    @Serializable(with = PublicKeyAs32ByteSerializer::class) val mintAuthority: PublicKey?,
    val supply: Long,
    val decimals: Int,
    val isInitialized: Boolean,
    val freezeAuthorityOption: Int,
    @Serializable(with = PublicKeyAs32ByteSerializer::class) val freezeAuthority: PublicKey?
)