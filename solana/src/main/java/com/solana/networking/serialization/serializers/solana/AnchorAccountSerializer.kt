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

open class DiscriminatorSerializer<T>(val discriminator: ByteArray, serializer: KSerializer<T>)
    : KSerializer<T> {

    private val accountSerializer = serializer
    override val descriptor: SerialDescriptor = accountSerializer.descriptor

    override fun serialize(encoder: Encoder, value: T) {
        discriminator.forEach { encoder.encodeByte(it) }
        accountSerializer.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): T {
        ByteArray(discriminator.size).map { decoder.decodeByte() }.apply {
            // should we/how can we check that the discriminator is correct?
//            check(discriminator contentEquals this.toByteArray()) {
//                "The decoded discriminant (${this.toByteArray().contentToString()}) differed from " +
//                        "the expected discriminant (${discriminator.contentToString()})."
//            }
        }
        return accountSerializer.deserialize(decoder)
    }
}

inline fun <reified A> ByteDiscriminatorSerializer(discriminator: Byte) =
    ByteDiscriminatorSerializer<A>(discriminator, serializer())

open class ByteDiscriminatorSerializer<T>(discriminator: Byte, serializer: KSerializer<T>)
    : DiscriminatorSerializer<T>(byteArrayOf(discriminator), serializer)

open class AnchorDiscriminatorSerializer<T>(namespace: String, ixName: String,
                                            serializer: KSerializer<T>)
    : DiscriminatorSerializer<T>(buildDiscriminator(namespace, ixName), serializer) {
    companion object {
        private fun buildDiscriminator(namespace: String, ixName: String) =
            MessageDigest.getInstance("SHA-256")
                .digest("$namespace:$ixName".toByteArray(StandardCharsets.UTF_8))
                .sliceArray(0 until 8)
    }
}

class AnchorAccountSerializer<T>(accountName: String, serializer: KSerializer<T>)
    : AnchorDiscriminatorSerializer<T>("account", accountName, serializer)

inline fun <reified A> AnchorAccountSerializer(accountName: String = A::class.java.simpleName) =
    AnchorAccountSerializer<A>(accountName, serializer())

class AnchorInstructionSerializer<T>(ixName: String, serializer: KSerializer<T>)
    : AnchorDiscriminatorSerializer<T>("global", ixName, serializer)

inline fun <reified A> AnchorInstructionSerializer(ixName: String) =
    AnchorInstructionSerializer<A>(ixName, serializer())