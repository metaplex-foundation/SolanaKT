package com.solana.models

import com.solana.api.SolanaAccountSerializer
import com.solana.core.PublicKey
import com.solana.core.PublicKeyRule
import com.solana.models.buffer.*
import com.solana.models.buffer.moshi.AccountInfoJsonAdapter
import com.solana.models.buffer.moshi.MintJsonAdapter
import com.solana.models.buffer.moshi.TokenSwapInfoJsonAdapter
import com.solana.networking.RpcResponseSerializable
import com.solana.networking.serialization.format.BorshDecoder
import com.solana.networking.serialization.format.BorshEncoder
import com.solana.networking.serialization.serializers.base64.BorshAsBase64JsonArraySerializer
import com.solana.networking.serialization.serializers.solana.AnchorAccountSerializer
import com.solana.networking.serialization.serializers.solana.AnchorInstructionSerializer
import com.solana.networking.serialization.serializers.solana.ByteDiscriminatorSerializer
import com.solana.networking.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.solana.vendor.borshj.Borsh
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.serialization.json.Json
import org.junit.Test
import org.junit.Assert.*
import java.util.*

class DecodingTests {

    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    @Test
    fun testDecodingMetaplexMeta() {
        val blobSrc = "[ \"BD8slOrgPZpL+5iD8MJBsTGY+esK0dZd8tlCX14LRmfREFsKKgyOBm9TbEDjhj2CcT+KPJFJ3acbYRLIqSsfsUkVAAAAU29sYW5pbWFsICMxOTYyIC0gQ2F0AAAAAD8AAABodHRwczovL2Fyd2VhdmUubmV0LzREVlNhUjU2V1RrV1Y4TnlvZ2U0LXRQWC1wOXJhSUJScHlZNEtxZVZlUDQAAAEBAAAAPyyU6uA9mkv7mIPwwkGxMZj56wrR1l3y2UJfXgtGZ9EBZAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA==\", \"base64\" ]"

        val serializer = BorshAsBase64JsonArraySerializer(
            MetaplexMeta.serializer()
        )
        val data = json.decodeFromString(serializer, blobSrc)!!

        assertEquals("5Fc7Zy7HgRatL8XhX5uqsUFEjGPop1uJXKrp3Ws7m1Tn", data.update_authority.toBase58())
        assertEquals("26r3GfgqbjMTjZahNgnwa9AtbDg8x2E5AGwzw17KWRC4", data.mint.toBase58())
        assertEquals("Solanimal #1962 - Cat", data.data.name)
        assertEquals("https://arweave.net/4DVSaR56WTkWV8Nyoge4-tPX-p9raIBRpyY4KqeVeP4", data.data.uri)
    }

    @Test
    fun testMoreDecodingMetaplexMeta() {

        val serializer = BorshAsBase64JsonArraySerializer(
            MetaplexMeta.serializer()
        )

        val plexData = MetaplexData("NFT", "BORSH", "www.solana.com")
        val plexMeta = MetaplexMeta(
            key = 1,
            update_authority = PublicKey("CiDwVBFgWV9E5MvXWoLgnEgn2hK7rJikbvfWavzAQz3"),
            mint = PublicKey("BQWWFhzBdw2vKKBUX17NHeFbCoFQHfRARpdztPE2tDJ"),
            data = plexData
        )

        val serialized = json.encodeToString(serializer, plexMeta)
        val out = json.decodeFromString(serializer, serialized)!!

        assertEquals("CiDwVBFgWV9E5MvXWoLgnEgn2hK7rJikbvfWavzAQz3", out.update_authority.toBase58())
        assertEquals("BQWWFhzBdw2vKKBUX17NHeFbCoFQHfRARpdztPE2tDJ", out.mint.toBase58())
        assertEquals("NFT", out.data.name)
        assertEquals("BORSH", out.data.symbol)
        assertEquals("www.solana.com", out.data.uri)
    }

    @Test
    fun testDecodingPublicKey() {
        val bytes = byteArrayOf( 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        val key = PublicKey(bytes)

        val encoder = BorshEncoder()

        assertEquals("CiDwVBFgWV9E5MvXWoLgnEgn2hK7rJikbvfWavzAQz3", key.toBase58())
        PublicKeyAs32ByteSerializer.serialize(encoder, key)

        assertEquals(encoder.borshEncodedBytes.toTypedArray(), bytes.toTypedArray())
        val decoder = BorshDecoder(encoder.borshEncodedBytes)
        val newPublickey = PublicKeyAs32ByteSerializer.deserialize(decoder)
        assertEquals("CiDwVBFgWV9E5MvXWoLgnEgn2hK7rJikbvfWavzAQz3", newPublickey.toBase58())
    }


    @Test
    fun testDecodingMint() {
        val rawData = "[ \"AQAAAAYa2dBThxVIU37ePiYYSaPft/0C+rx1siPI5GrbhT0MABCl1OgAAAAGAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA==\", \"base64\" ]"
        val serializer = BorshAsBase64JsonArraySerializer(
            AnchorAccountSerializer(Mint.serializer().descriptor.serialName, Mint.serializer())
        )
        val data = json.decodeFromString(serializer, rawData)
        assertNotNull(data)
        val mintLayout = data!!

        assertEquals(
            "9YEvWTS28MsTU6rzpeHQ5RsY9oVPdZSfbFgWn3nZf9Sm",
            mintLayout.mintAuthority!!.toBase58()
        )
        assertEquals(1, mintLayout.supply)
        assertEquals(0, mintLayout.decimals)
        assertTrue(!mintLayout.isInitialized)
        assertNull(mintLayout.freezeAuthority)

        val serialized = json.encodeToString(serializer, mintLayout)
        val deserialized = json.decodeFromString(serializer, serialized)!!

        assertEquals(deserialized.mintAuthority!!.toBase58(), mintLayout.mintAuthority!!.toBase58())
        assertEquals(deserialized.supply, mintLayout.supply)
        assertEquals(deserialized.decimals, mintLayout.decimals)
        assertTrue(deserialized.isInitialized == mintLayout.isInitialized)
        assertEquals(deserialized.freezeAuthority, mintLayout.freezeAuthority)
    }

    @Test
    fun testDecodingAccountInfo() {
        val rawData = "[\"BhrZ0FOHFUhTft4+JhhJo9+3/QL6vHWyI8jkatuFPQwCqmOzhzy1ve5l2AqL0ottCChJZ1XSIW3k3C7TaBQn7aCGAQAAAAAAAQAAAOt6vNDYdevCbaGxgaMzmz7yoxaVu3q9vGeCc7ytzeWqAQAAAAAAAAAAAAAAAGQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\", \"base64\"]"
        val serializer = BorshAsBase64JsonArraySerializer(AccountInfoData.serializer())
        val data = json.decodeFromString(serializer, rawData)
        assertNotNull(data)
        val accountInfo = data!!
        assertEquals("QqCCvshxtqMAL2CVALqiJB7uEeE5mjSPsseQdDzsRUo", accountInfo.mint.toBase58())
        assertEquals("BQWWFhzBdw2vKKBUX17NHeFbCoFQHfRARpdztPE2tDJ", accountInfo.owner.toBase58())
        assertEquals(
            "9G7jGckmQwx38DeJYqFiPZNZJ2AyGM6XEL9d9pLyZ1yr",
            accountInfo.delegate?.toBase58()
        )
        assertEquals(0, accountInfo.delegatedAmount)
        assertEquals(false, accountInfo.isNative)
        assertEquals(false, accountInfo.isInitialized)
        assertEquals(false, accountInfo.isFrozen)
        assertNull(accountInfo.rentExemptReserve)
        assertNull(accountInfo.closeAuthority)

        val serialized = json.encodeToString(serializer, accountInfo)
        val deserialized = json.decodeFromString(serializer, serialized)!!

        assertEquals(deserialized.mint.toBase58(), accountInfo.mint.toBase58())
        assertEquals(deserialized.owner.toBase58(), accountInfo.owner.toBase58())
        assertEquals(deserialized.delegate?.toBase58(), accountInfo.delegate?.toBase58())
        assertEquals(deserialized.state, accountInfo.state)
        assertEquals(deserialized.isNativeOption, accountInfo.isNativeOption)
        assertEquals(deserialized.rentExemptReserve, accountInfo.rentExemptReserve)
        assertEquals(deserialized.isNativeRaw, accountInfo.isNativeRaw)
        assertEquals(deserialized.isNative, accountInfo.isNative)
        assertEquals(deserialized.isInitialized, accountInfo.isInitialized)
        assertEquals(deserialized.delegatedAmount, accountInfo.delegatedAmount)
        assertEquals(deserialized.isInitialized, accountInfo.isInitialized)
        assertEquals(deserialized.isFrozen, accountInfo.isFrozen)
        assertEquals(deserialized.closeAuthority, accountInfo.closeAuthority)
    }


    @Test
    fun testDecodingAccountInfo2() {
        val rawData = "[\"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAOt6vNDYdevCbaGxgaMzmz7yoxaVu3q9vGeCc7ytzeWq\", \"base64\"]"
        val serializer = BorshAsBase64JsonArraySerializer(AccountInfoData.serializer())
        val data = json.decodeFromString(serializer, rawData)
        assertNotNull(data)
        val accountInfo = data!!

        assertNull(accountInfo.delegate)
        assertEquals(0, accountInfo.delegatedAmount)
        assertEquals(false, accountInfo.isInitialized)
        assertEquals(false, accountInfo.isNative)
        assertNull(accountInfo.rentExemptReserve)
    }
}