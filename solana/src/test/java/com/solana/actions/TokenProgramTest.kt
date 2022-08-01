package com.solana.actions


import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.programs.TokenProgram
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.*


class TokenProgramTest {
    @Test
    fun initializeMint() {
        val instruction = TokenProgram.initializeMint(MINT_ACCOUNT_SIGNER.publicKey, 0, MINT_AUTHORITY);

        val serializedTransaction = with(Transaction()) {
            addInstruction(instruction)
            setRecentBlockHash("Eit7RCyhUixAe2hGBS8oqnw59QK3kgMMjfLME5bm9wRn")
            sign(MINT_ACCOUNT_SIGNER)
            serialize()
        }

        assertEquals(
            "AUoR2pOLvCw+4HBuJeRwiFZrQEUwxGxjwYL7lt7Ml7+gnqJ5GDKXPZzqc86enRU/eWrbrTwjTqnwtvCzTorbyQwBAAIDdzBsVYjcOAdulE3ZeRcnn0fqEGjydBJqwCb++mjnoPAGp9UXGSxcUSGMyUw9SvF/WNruCJuh/UTj29mKAAAAAAbd9uHXZaGT2cvhRs7reawctIXtX1s3kTqM9YV+/wCpy+KIwZmU8DLmYglP3bPzrlpDaKkGu6VIJJwTOYQmRfUBAgIAAUMAAHliGrHMD2/tnxlfFt004e78Gx01J8/pMuF7TJD14g6yAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            Base64.getEncoder().encodeToString(serializedTransaction)
        )
    }

    @Test
    fun initializeMint_withFreezeAuthority() {
        val instruction = TokenProgram.initializeMint(MINT_ACCOUNT_SIGNER.publicKey, 0, MINT_AUTHORITY, FREEZE_AUTHORITY);

        val serializedTransaction = with(Transaction()) {
            addInstruction(instruction)
            setRecentBlockHash("Eit7RCyhUixAe2hGBS8oqnw59QK3kgMMjfLME5bm9wRn")
            sign(MINT_ACCOUNT_SIGNER)
            serialize()
        }

        assertEquals(
            "Aa/cfiQCH2/Q1hGyG9bZYvmcMvJH+Y0IzAnA1GuhnqlzRUFzRpR261HQaUYXvW0VNPA8pvqrOIHtK0Ks4kemSQoBAAIDdzBsVYjcOAdulE3ZeRcnn0fqEGjydBJqwCb++mjnoPAGp9UXGSxcUSGMyUw9SvF/WNruCJuh/UTj29mKAAAAAAbd9uHXZaGT2cvhRs7reawctIXtX1s3kTqM9YV+/wCpy+KIwZmU8DLmYglP3bPzrlpDaKkGu6VIJJwTOYQmRfUBAgIAAUMAAHliGrHMD2/tnxlfFt004e78Gx01J8/pMuF7TJD14g6yAQ8o+2LHwHas60HhK0I2UCPRN1mzPCOeLzEIW4SoYdJ9",
            Base64.getEncoder().encodeToString(serializedTransaction)
        )
    }

    @Test
    fun initializeMint_withFreezeAuthorityAnd9Decimals() {
        val instruction = TokenProgram.initializeMint(MINT_ACCOUNT_SIGNER.publicKey, 9, MINT_AUTHORITY, FREEZE_AUTHORITY);

        val serializedTransaction = with(Transaction()) {
            addInstruction(instruction)
            setRecentBlockHash("Eit7RCyhUixAe2hGBS8oqnw59QK3kgMMjfLME5bm9wRn")
            sign(MINT_ACCOUNT_SIGNER)
            serialize()
        }

        assertEquals(
            "ATZzZcv9ZRHTIoqeRP79u4kX9HdkluOCICsV001wKCNAZhAIKc6jqXNDJVTxoLIVRq5Dltt/bdfW1H7DJ94rOAMBAAIDdzBsVYjcOAdulE3ZeRcnn0fqEGjydBJqwCb++mjnoPAGp9UXGSxcUSGMyUw9SvF/WNruCJuh/UTj29mKAAAAAAbd9uHXZaGT2cvhRs7reawctIXtX1s3kTqM9YV+/wCpy+KIwZmU8DLmYglP3bPzrlpDaKkGu6VIJJwTOYQmRfUBAgIAAUMACXliGrHMD2/tnxlfFt004e78Gx01J8/pMuF7TJD14g6yAQ8o+2LHwHas60HhK0I2UCPRN1mzPCOeLzEIW4SoYdJ9",
            Base64.getEncoder().encodeToString(serializedTransaction)
        )
    }

    companion object {
        // public key: 92GLpcVjbC1dA4TNRrb6ooNQGj7iqYk4bR1Xvwat2Wkf
        private val MINT_SECRET_KEY = intArrayOf(248, 245, 206, 215, 221, 248, 207, 125, 63, 204, 25, 40, 10, 180, 174,
            189, 124, 221, 111, 20, 34, 34, 147, 0, 194, 55, 115, 203, 41, 9, 128,
            188, 119, 48, 108, 85, 136, 220, 56, 7, 110, 148, 77, 217, 121, 23, 39,
            159, 71, 234, 16, 104, 242, 116, 18, 106, 192, 38, 254, 250, 104, 231,
            160, 240).map(Int::toByte).toByteArray()
        private val MINT_ACCOUNT_SIGNER = Account(MINT_SECRET_KEY)

        private val MINT_AUTHORITY = PublicKey("9Aq6XkUT8Nx2ztkpkUxc4HiVCFWKTJZWiLnhC94iofvy")
        private val FREEZE_AUTHORITY = PublicKey("22BMtVRGveoYeJyvYDHfJP5JMCRKTYbp3QMhxU1P8w1n")
    }
}
