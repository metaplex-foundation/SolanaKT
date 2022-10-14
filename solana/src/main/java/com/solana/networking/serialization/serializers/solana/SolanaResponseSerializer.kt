/*
 * SolanaResponseSerializer
 * metaplex-android
 * 
 * Created by Funkatronics on 7/31/2022
 */

package com.solana.networking.serialization.serializers.solana

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


class SolanaResponseSerializer<R>(dataSerializer: KSerializer<R>)
    : KSerializer<R?> {
    private val serializer = WrappedValue.serializer(dataSerializer)
    override val descriptor: SerialDescriptor = serializer.descriptor

    override fun serialize(encoder: Encoder, value: R?) =
        encoder.encodeSerializableValue(serializer, WrappedValue(value))

    override fun deserialize(decoder: Decoder): R? =
        decoder.decodeSerializableValue(serializer).value
}

@Serializable
private class WrappedValue<V>(val value: V?)