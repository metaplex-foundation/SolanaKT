package com.solana.networking.serialization.serializers.base58

import com.solana.networking.serialization.format.BorshDecoder
import com.solana.networking.serialization.format.BorshEncoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bitcoinj.core.Base58


/**
 * (De)Serializes any array of bytes as a Base58 encoded string, formatted as Json string array:
 * output = {
 *      [
 *          "theBase58EncodedString",
 *          "base58"
 *      ]
 * }
 */
object ByteArrayAsBase58JsonArraySerializer: KSerializer<ByteArray> {
    private val delegateSerializer = ListSerializer(String.serializer())
    override val descriptor: SerialDescriptor = delegateSerializer.descriptor

    override fun serialize(encoder: Encoder, value: ByteArray) =
        encoder.encodeSerializableValue(
            delegateSerializer, listOf(
                Base58.encode(value), "base58"
            ))

    override fun deserialize(decoder: Decoder): ByteArray {
        decoder.decodeSerializableValue(delegateSerializer).apply {
            if (contains("base58")) first { it != "base58" }.apply {
                return Base58.decode(this)
            }
            else throw(SerializationException("Not Base58"))
        }
    }
}

/**
 * Decodes/Encodes input using the Borsh encoding scheme, and serializes it as a Base58 encoded
 * string, formatted as Json string array:
 * output = {
 *      [
 *          "theBorshEncodedBytesAsBase58String",
 *          "base58"
 *      ]
 * }
 */
class BorshAsBase58JsonArraySerializer<T>(private val dataSerializer: KSerializer<T>):
    KSerializer<T?> {
    private val delegateSerializer = ByteArrayAsBase58JsonArraySerializer
    override val descriptor: SerialDescriptor = dataSerializer.descriptor

    override fun serialize(encoder: Encoder, value: T?) =
        encoder.encodeSerializableValue(delegateSerializer,
            value?.let {
                BorshEncoder().apply {
                    encodeSerializableValue(dataSerializer, value)
                }.borshEncodedBytes
            } ?: byteArrayOf()
        )

    override fun deserialize(decoder: Decoder): T? =
        decoder.decodeSerializableValue(delegateSerializer).run {
            if (this.isEmpty()) return null
            BorshDecoder(this).decodeSerializableValue(dataSerializer)
        }
}