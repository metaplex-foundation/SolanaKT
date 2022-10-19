package com.solana.models.buffer

import com.solana.core.PublicKey
import com.solana.core.PublicKeyRule
import com.solana.networking.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.solana.vendor.borshj.*
import kotlinx.serialization.Serializable
import java.lang.Exception

@Serializable
data class Mint(
    val mintAuthorityOption: Int,
    @Serializable(with = PublicKeyAs32ByteSerializer::class) val mintAuthority: PublicKey?,
    val supply: Long,
    val decimals: Int,
    val isInitialized: Boolean,
    val freezeAuthorityOption: Int,
    @Serializable(with = PublicKeyAs32ByteSerializer::class) val freezeAuthority: PublicKey?
) : BorshCodable

class MintRule(override val clazz: Class<Mint> = Mint::class.java): BorshRule<Mint> {
    override fun read(input: BorshInput): Mint {
        val mintAuthorityOption: Int = input.readU32()
        var mintAuthority: PublicKey? = try { PublicKeyRule().read(input) } catch (e : Exception) { null }
        val supply: Long = input.readU64()
        val decimals: Int = input.read().toInt()
        val isInitialized: Boolean = decimals != 1
        val freezeAuthorityOption: Int = input.readU32()
        var freezeAuthority: PublicKey? = try {
            if(input.readFixedArray(32).contentEquals(ByteArray(32))){
                null
            } else {
                PublicKeyRule().read(input)
            }
        } catch (e : Exception) {
            null
        }

        if(mintAuthorityOption == 0){
            mintAuthority = null
        }

        if(freezeAuthorityOption == 0){
            freezeAuthority = null
        }

        return Mint(
            mintAuthorityOption,
            mintAuthority,
            supply,
            decimals,
            isInitialized,
            freezeAuthorityOption,
            freezeAuthority
        )
    }

    override fun <Self>write(obj: Any, output: BorshOutput<Self>): Self {
        val mint = obj as Mint
        output.writeU32(mint.mintAuthorityOption)
        mint.mintAuthority?.let {
            PublicKeyRule().write(it, output)
        } ?: run {
            PublicKeyRule().writeZeros(output)
        }
        output.writeU64(mint.supply)
        output.write(mint.decimals.toByte())
        output.writeU32(mint.freezeAuthorityOption)
        return mint.freezeAuthority?.let {
            PublicKeyRule().write(it, output)
        } ?: run {
            PublicKeyRule().writeZeros(output)
        }
    }
}