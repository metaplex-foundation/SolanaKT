/*
 * AnchorAccountSerializer
 * Metaplex
 * 
 * Created by Funkatronics on 7/25/2022
 */

package com.solana.networking.serialization.serializers.solana

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class AnchorAccountSerializer<T>(accountName: String, serializer: KSerializer<T>)
    : KSerializer<T> {
    private val accountSerializer = serializer
    private val discriminator: ByteArray = MessageDigest.getInstance("SHA-256")
        .digest("account:$accountName".toByteArray(StandardCharsets.UTF_8)).copyOfRange(0, 8)

    override val descriptor: SerialDescriptor = accountSerializer.descriptor

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeLong(ByteBuffer.wrap(discriminator).order(ByteOrder.LITTLE_ENDIAN).long)
        accountSerializer.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): T {
        decoder.decodeLong() // should we check that the discriminator is correct?
        return accountSerializer.deserialize(decoder)
    }
}

inline fun <reified A> AnchorAccountSerializer(accountName: String = A::class.java.simpleName) =
    AnchorAccountSerializer<A>(accountName, serializer())