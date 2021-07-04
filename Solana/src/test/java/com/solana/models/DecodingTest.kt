package com.solana.models

import com.solana.core.PublicKey
import com.solana.models.Buffer.*
import com.solana.vendor.borshj.Borsh
import org.junit.Test
import org.junit.Assert.*

class DecodingTests {
    val borsh = Borsh()

    @Test
    fun testDecodingPublicKey() {
        val bytes = byteArrayOf(3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        val key = PublicKey(bytes)
        assertEquals("CiDwVBFgWV9E5MvXWoLgnEgn2hK7rJikbvfWavzAQz3", key.toBase58())
        val serialized = borsh.serialize(key)
        assertEquals(serialized.toTypedArray(), bytes.toTypedArray())
        val newPublickey = borsh.deserialize(serialized, PublicKey::class.java)
        assertEquals("CiDwVBFgWV9E5MvXWoLgnEgn2hK7rJikbvfWavzAQz3", newPublickey.toBase58())
    }


    @Test
    fun testDecodingMint() {
        val rawData = listOf("AQAAAAYa2dBThxVIU37ePiYYSaPft/0C+rx1siPI5GrbhT0MABCl1OgAAAAGAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA==","base64")
        val buffer = Buffer(borsh, rawData, Mint::class.java)
        assertNotNull(buffer.value)
        val mintLayout = buffer.value!!

        assertEquals("QqCCvshxtqMAL2CVALqiJB7uEeE5mjSPsseQdDzsRUo", mintLayout.mintAuthority!!.toBase58())
        assertEquals(1000000000000, mintLayout.supply)
        assertEquals(mintLayout.decimals, 6)
        assertTrue(mintLayout.isInitialized == true)
        assertNull(mintLayout.freezeAuthority)
    }

    @Test
    fun testDecodingAccountInfo() {
        val rawData: List<String> = listOf("BhrZ0FOHFUhTft4+JhhJo9+3/QL6vHWyI8jkatuFPQwCqmOzhzy1ve5l2AqL0ottCChJZ1XSIW3k3C7TaBQn7aCGAQAAAAAAAQAAAOt6vNDYdevCbaGxgaMzmz7yoxaVu3q9vGeCc7ytzeWqAQAAAAAAAAAAAAAAAGQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA","base64")
        val buffer = Buffer(borsh, rawData, AccountInfo::class.java)
        assertNotNull(buffer.value)
        val accountInfo = buffer.value!!
        assertEquals("QqCCvshxtqMAL2CVALqiJB7uEeE5mjSPsseQdDzsRUo", accountInfo.mint.toBase58())
        assertEquals("BQWWFhzBdw2vKKBUX17NHeFbCoFQHfRARpdztPE2tDJ", accountInfo.owner.toBase58())
        assertEquals("GrDMoeqMLFjeXQ24H56S1RLgT4R76jsuWCd6SvXyGPQ5", accountInfo.delegate?.toBase58())
        assertEquals(100, accountInfo.delegatedAmount)
        assertEquals(false, accountInfo.isNative)
        assertEquals(true, accountInfo.isInitialized)
        assertEquals(false, accountInfo.isFrozen)
        assertNull(accountInfo.rentExemptReserve)
        assertNull(accountInfo.closeAuthority)

        val serialized = borsh.serialize(accountInfo)

        val deserialized = borsh.deserialize(serialized, AccountInfo::class.java)

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
        val string = listOf("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAOt6vNDYdevCbaGxgaMzmz7yoxaVu3q9vGeCc7ytzeWq","base64")
        val buffer = Buffer(borsh, string, AccountInfo::class.java)
        assertNotNull(buffer.value)
        val accountInfo = buffer.value!!

        assertNull(accountInfo.delegate)
        assertEquals(0, accountInfo.delegatedAmount)
        assertEquals(false, accountInfo.isInitialized)
        assertEquals(false, accountInfo.isNative)
        assertNull(accountInfo.rentExemptReserve)

        assertEquals("GrDMoeqMLFjeXQ24H56S1RLgT4R76jsuWCd6SvXyGPQ5", accountInfo.closeAuthority?.toBase58())

        val string2 = listOf("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAOt6vNDYdevCbaGxgaMzmz7yoxaVu3q9vGeCc7ytzeWq","base64")
        val buffer2 = Buffer(borsh, string2, AccountInfo::class.java)
        assertNotNull(buffer.value)
        val accountInfo2 = buffer2.value!!
        assertEquals(true, accountInfo2.isFrozen)
    }
}