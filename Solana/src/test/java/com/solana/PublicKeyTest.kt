package com.solana

import com.solana.models.PublicKey
import org.junit.Assert.*
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.util.*


class PublicKeyTest {
    @Test(expected = IllegalArgumentException::class)
    fun ivalidKeys() {
        PublicKey(
            byteArrayOf(
                3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0
            )
        )
        PublicKey("300000000000000000000000000000000000000000000000000000000000000000000")
        PublicKey("300000000000000000000000000000000000000000000000000000000000000")
    }

    @Test
    fun validKeys() {
        val key = PublicKey(
            byteArrayOf(
                3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0
            )
        )
        assertEquals("CiDwVBFgWV9E5MvXWoLgnEgn2hK7rJikbvfWavzAQz3", key.toString())
        val key1 = PublicKey("CiDwVBFgWV9E5MvXWoLgnEgn2hK7rJikbvfWavzAQz3")
        assertEquals("CiDwVBFgWV9E5MvXWoLgnEgn2hK7rJikbvfWavzAQz3", key1.toBase58())
        val key2 = PublicKey("11111111111111111111111111111111")
        assertEquals("11111111111111111111111111111111", key2.toBase58())
        val byteKey = byteArrayOf(
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 1
        )
        val key3 = PublicKey(byteKey)
        assertArrayEquals(byteKey, PublicKey(key3.toBase58()).toByteArray())
    }

    @Test
    fun equals() {
        val key = PublicKey("11111111111111111111111111111111")
        assertTrue(key.equals(key))
        assertFalse(key.equals(PublicKey("11111111111111111111111111111112")))
    }

    @Test
    fun readPubkey() {
        val key = PublicKey("11111111111111111111111111111111")
        val bos = ByteArrayOutputStream()
        bos.write(1)
        bos.write(key.toByteArray())
        val bytes = bos.toByteArray()
        assertEquals(key.toString(), PublicKey.readPubkey(bytes, 1).toString())
    }

    @Test
    @Throws(Exception::class)
    fun createProgramAddress() {
        val programId = PublicKey("BPFLoader1111111111111111111111111111111111")
        var programAddress = PublicKey.createProgramAddress(
            Arrays.asList(PublicKey("SeedPubey1111111111111111111111111111111111").toByteArray()),
            programId
        )
        assertTrue(programAddress.equals(PublicKey("GUs5qLUfsEHkcMB9T38vjr18ypEhRuNWiePW2LoK4E3K")))
        programAddress = PublicKey.createProgramAddress(
            Arrays.asList("".toByteArray(), byteArrayOf(1)),
            programId
        )
        assertTrue(programAddress.equals(PublicKey("3gF2KMe9KiC6FNVBmfg9i267aMPvK37FewCip4eGBFcT")))
        programAddress = PublicKey.createProgramAddress(Arrays.asList("☉".toByteArray()), programId)
        assertTrue(programAddress.equals(PublicKey("7ytmC1nT1xY4RfxCV2ZgyA7UakC93do5ZdyhdF3EtPj7")))
        programAddress = PublicKey.createProgramAddress(
            Arrays.asList("Talking".toByteArray(), "Squirrels".toByteArray()),
            programId
        )
        assertTrue(programAddress.equals(PublicKey("HwRVBufQ4haG5XSgpspwKtNd3PC9GM9m1196uJW36vds")))
        val programAddress2 =
            PublicKey.createProgramAddress(Arrays.asList("Talking".toByteArray()), programId)
        assertFalse(programAddress.equals(programAddress2))
    }

    @Test
    @Throws(Exception::class)
    fun findProgramAddress() {
        val programId = PublicKey("BPFLoader1111111111111111111111111111111111")
        val programAddress =
            PublicKey.findProgramAddress(Arrays.asList("".toByteArray()), programId)
        assertTrue(
            programAddress.address.equals(
                PublicKey.createProgramAddress(
                    Arrays.asList("".toByteArray(), byteArrayOf(programAddress.nonce.toByte())),
                    programId
                )
            )
        )
    }

    @Test
    @Throws(Exception::class)
    fun findProgramAddress1() {
        val programId = PublicKey("6Cust2JhvweKLh4CVo1dt21s2PJ86uNGkziudpkNPaCj")
        val programId2 = PublicKey("BPFLoader1111111111111111111111111111111111")
        val programAddress = PublicKey.findProgramAddress(
            Arrays.asList(PublicKey("8VBafTNv1F8k5Bg7DTVwhitw3MGAMTmekHsgLuMJxLC8").toByteArray()),
            programId
        )
        assertTrue(programAddress.address.equals(PublicKey("FGnnqkzkXUGKD7wtgJCqTemU3WZ6yYqkYJ8xoQoXVvUG")))
        val programAddress2 = PublicKey
            .findProgramAddress(
                Arrays.asList(
                    PublicKey("SeedPubey1111111111111111111111111111111111").toByteArray(),
                    PublicKey("3gF2KMe9KiC6FNVBmfg9i267aMPvK37FewCip4eGBFcT").toByteArray(),
                    PublicKey("HwRVBufQ4haG5XSgpspwKtNd3PC9GM9m1196uJW36vds").toByteArray()
                ),
                programId2
            )
        assertTrue(programAddress2.address.equals(PublicKey("GXLbx3CbJuTTtJDZeS1PGzwJJ5jGYVEqcXum7472kpUp")))
        assertEquals(programAddress2.nonce, 254)
    }
}