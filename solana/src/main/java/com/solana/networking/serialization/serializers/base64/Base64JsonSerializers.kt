/*
 * Base64JsonSerializers
 * Metaplex
 * 
 * Created by Funkatronics on 7/22/2022
 */

package com.solana.networking.serialization.serializers.base64

import com.solana.networking.serialization.format.BorshDecoder
import com.solana.networking.serialization.format.BorshEncoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bouncycastle.util.encoders.Base64

/**
 * (De)Serializes any array of bytes as a Base64 encoded string, formatted as Json string array:
 * output = {
 *      [
 *          "theBase64EncodedString",
 *          "base64"
 *      ]
 * }
 */
object ByteArrayAsBase64JsonArraySerializer: KSerializer<ByteArray> {
    private val delegateSerializer = ListSerializer(String.serializer())
    override val descriptor: SerialDescriptor = delegateSerializer.descriptor

    override fun serialize(encoder: Encoder, value: ByteArray) =
        encoder.encodeSerializableValue(
            delegateSerializer, listOf(
                Base64.toBase64String(value), "base64"
        ))

    override fun deserialize(decoder: Decoder): ByteArray {
        decoder.decodeSerializableValue(delegateSerializer).apply {
            if (contains("base64")) first { it != "base64" }.apply {
                return Base64.decode(this)
            }
            else throw(SerializationException("Not Base64"))
        }
    }
}

/**
 * Decodes/Encodes input using the Borsh encoding scheme, and serializes it as a Base64 encoded
 * string, formatted as Json string array:
 * output = {
 *      [
 *          "theBorshEncodedBytesAsBase64String",
 *          "base64"
 *      ]
 * }
 */
class BorshAsBase64JsonArraySerializer<T>(private val dataSerializer: KSerializer<T>): KSerializer<T?> {
    private val delegateSerializer = ByteArrayAsBase64JsonArraySerializer
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