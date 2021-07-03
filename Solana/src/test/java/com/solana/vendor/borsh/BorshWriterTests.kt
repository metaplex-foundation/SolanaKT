package com.solana.vendor.borsh

import com.solana.vendor.borshj.BorshWriter
import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayOutputStream

class BorshWriterTests {
    private var output: ByteArrayOutputStream? = null
    private var writer: BorshWriter? = null
    fun newWriter() {
        output = ByteArrayOutputStream()
        writer = BorshWriter(output!!)
    }

    @Test
    fun captureOutput() {
        newWriter()
        writer!!.writeString("Borsh")
        Assert.assertArrayEquals(
            byteArrayOf(
                5,
                0,
                0,
                0,
                'B'.code.toByte(),
                'o'.code.toByte(),
                'r'.code.toByte(),
                's'.code.toByte(),
                'h'.code.toByte()
            ), output!!.toByteArray()
        )
    }
}