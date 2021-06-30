package com.solana.models

import com.solana.models.Buffer.AccountInfoData
import com.solana.models.Buffer.AccountInfoLayout
import com.solana.models.Buffer.Buffer
import org.junit.Assert
import org.junit.Test
import org.junit.Assert.*

class DecodingTests {

    @Test
    fun testDecodingAccountInfo() {
        val rawData: List<String> = listOf("BhrZ0FOHFUhTft4+JhhJo9+3/QL6vHWyI8jkatuFPQwCqmOzhzy1ve5l2AqL0ottCChJZ1XSIW3k3C7TaBQn7aCGAQAAAAAAAQAAAOt6vNDYdevCbaGxgaMzmz7yoxaVu3q9vGeCc7ytzeWqAQAAAAAAAAAAAAAAAGQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA","base64")
        val buffer = Buffer(rawData, AccountInfoLayout().layout, AccountInfoData::class.java)
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
    }
}