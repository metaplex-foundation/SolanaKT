/*
 * SolanaRpcSerializers
 * Metaplex
 * 
 * Created by Funkatronics on 7/22/2022
 */

package com.solana.networking

import com.solana.api.AccountInfo
import com.solana.core.PublicKey
import com.solana.networking.serialization.serializers.base64.BorshAsBase64JsonArraySerializer
import com.solana.networking.serialization.serializers.solana.AnchorAccountSerializer
import com.solana.networking.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.solana.networking.serialization.serializers.solana.SolanaResponseSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable

@Serializable
data class AccountInfoWithPublicKey<P>(val account: AccountInfo<P>, @SerialName("pubkey") val publicKey: String)

@Serializable
data class AccountPublicKey(@Serializable(with = PublicKeyAs32ByteSerializer::class) val publicKey: PublicKey)

internal fun <A> MultipleAccountsSerializer(serializer: KSerializer<A>) =
    MultipleAccountsInfoSerializer(
        BorshAsBase64JsonArraySerializer(
            AnchorAccountSerializer(serializer.descriptor.serialName, serializer)
        )
    )

internal fun <A> ProgramAccountsSerializer(serializer: KSerializer<A>) =
    ListSerializer(
        AccountInfoWithPublicKey.serializer(
            BorshAsBase64JsonArraySerializer(serializer)
        ).nullable
    )

internal inline fun <reified A> MultipleAccountsSerializer() =
    MultipleAccountsInfoSerializer<A?>(BorshAsBase64JsonArraySerializer(AnchorAccountSerializer()))


private fun <D> MultipleAccountsInfoSerializer(serializer: KSerializer<D>) =
    SolanaResponseSerializer(ListSerializer(AccountInfo.serializer(serializer).nullable))