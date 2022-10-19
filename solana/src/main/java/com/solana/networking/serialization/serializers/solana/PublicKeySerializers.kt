/*
 * PublicKeySerializers
 * Metaplex
 * 
 * Created by Funkatronics on 7/20/2022
 */

package com.solana.networking.serialization.serializers.solana

import com.solana.core.PublicKey
import com.solana.networking.serialization.format.BorshDecoder
import com.solana.networking.serialization.format.BorshEncoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object PublicKeyAs32ByteSerializer : KSerializer<PublicKey> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("PublicKey")

    override fun serialize(encoder: Encoder, value: PublicKey) =
        if (encoder is BorshEncoder) value.toByteArray().forEach { b -> encoder.encodeByte(b) }
        else encoder.encodeSerializableValue(ByteArraySerializer(), value.toByteArray())

    override fun deserialize(decoder: Decoder): PublicKey =
        if (decoder is BorshDecoder) PublicKey((0 until 32).map { decoder.decodeByte() }.toByteArray())
        else PublicKey(decoder.decodeSerializableValue(ByteArraySerializer()))
}

object PublicKeyAsStringSerializer : KSerializer<PublicKey> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("PublicKey")

    override fun serialize(encoder: Encoder, value: PublicKey) = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): PublicKey = PublicKey(decoder.decodeString())
}