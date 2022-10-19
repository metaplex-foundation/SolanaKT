/*
 * BorshCodeableaSerializer
 * metaplex-android
 * 
 * Created by Funkatronics on 8/1/2022
 */

package com.solana.networking.serialization.serializers.legacy

import com.solana.core.AccountPublicKeyRule
import com.solana.core.PublicKeyRule
import com.solana.models.buffer.AccountInfoRule
import com.solana.models.buffer.MintRule
import com.solana.models.buffer.TokenSwapInfoRule
import com.solana.networking.serialization.format.BorshDecoder
import com.solana.networking.serialization.format.BorshEncoder
import com.solana.vendor.borshj.BorshBuffer
import com.solana.vendor.borshj.BorshCodable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializer(forClass = BorshCodable::class)
class BorshCodeableSerializer<T>(val clazz: Class<T>) : KSerializer<BorshCodable?> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(clazz.simpleName) {}

    val rule = listOf(
        AccountPublicKeyRule(),
        PublicKeyRule(),
        AccountInfoRule(),
        MintRule(),
        TokenSwapInfoRule()
    ).find { it.clazz == clazz }

    override fun deserialize(decoder: Decoder): BorshCodable? =
        rule?.read(BorshBuffer.wrap((decoder as? BorshDecoder)?.bytes))

    override fun serialize(encoder: Encoder, value: BorshCodable?) {
        value?.let {
            rule?.write(value, BorshBuffer.wrap((encoder as? BorshEncoder)?.borshEncodedBytes))
        }
    }
}