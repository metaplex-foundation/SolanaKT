package com.solana.vendor.borsh

import com.solana.vendor.borshj.BorshReader
import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayInputStream

class BorshReaderTests {
    private var input: ByteArrayInputStream? = null
    private var reader: BorshReader? = null
    protected fun newReader(bytes: ByteArray?): BorshReader {
        input = ByteArrayInputStream(bytes)
        reader = BorshReader(input!!)
        return reader!!
    }

    @Test
    fun parseInput() {
        Assert.assertEquals(
            "Borsh",
            newReader(
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
                )
            ).readString()
        )
    }
}