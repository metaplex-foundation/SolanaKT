package com.solana.programs


import com.solana.core.HotAccount
import com.solana.core.PublicKey
import com.solana.core.Transaction
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.*


class TokenProgramTest {

    @Test
    fun sanityChecks() {
        assertEquals(
            MINT_ACCOUNT_SIGNER.publicKey.toString(),
            "92GLpcVjbC1dA4TNRrb6ooNQGj7iqYk4bR1Xvwat2Wkf"
        );
        assertEquals(
            DESTINATION_KEY_PAIR.publicKey.toString(),
            "HmzQ2Qy4UAJLyYBMuA56ErzXL5DAnNQzaMNTzGKPHdQA"
        );
        assertEquals(
            MINT_AUTHORITY_KEYPAIR_CboH.publicKey.toString(),
            "CboHzBWtkrx2a8NuJAT7yAQBcviKz944ihbufZAx3ZpH"
        );
    }

    @Test
    fun initializeMint() {
        val instruction =
            TokenProgram.initializeMint(MINT_ACCOUNT_SIGNER.publicKey, 0, MINT_AUTHORITY_PUBLIC_KEY_9Aq6);

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
        val instruction = TokenProgram.initializeMint(
            MINT_ACCOUNT_SIGNER.publicKey,
            0,
            MINT_AUTHORITY_PUBLIC_KEY_9Aq6,
            FREEZE_AUTHORITY
        );

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
        val instruction = TokenProgram.initializeMint(
            MINT_ACCOUNT_SIGNER.publicKey,
            9,
            MINT_AUTHORITY_PUBLIC_KEY_9Aq6,
            FREEZE_AUTHORITY
        );

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

    @Test
    fun mintTo() {
        val instruction = TokenProgram.mintTo(
            mint = MINT_ACCOUNT_SIGNER.publicKey,
            destination = DESTINATION_KEY_PAIR.publicKey,
            mintAuthority = MINT_AUTHORITY_KEYPAIR_CboH.publicKey,
            amount = 1L,
        );

        val serializedTransaction = with(Transaction()) {
            addInstruction(instruction)
            setRecentBlockHash("Eit7RCyhUixAe2hGBS8oqnw59QK3kgMMjfLME5bm9wRn")
            sign(MINT_AUTHORITY_KEYPAIR_CboH)
            serialize()
        }

        assertEquals(
            "AfRYDax10KJuroMG5AvKPWlZUVOg6bGsPXK8n/gorLCUf3YzJMeAa/8klZukCt68/Xio975Byveg2wn8FG0o+QUBAAEErFqlrwrywtwjXsQB1O1cieb2oVvQ/GC+KL+QkD1OS5Z3MGxViNw4B26UTdl5FyefR+oQaPJ0EmrAJv76aOeg8PlBcgStmhLxhpL3FragO9vsGMIBV/pu+lpbifkTgF2vBt324ddloZPZy+FGzut5rBy0he1fWzeROoz1hX7/AKnL4ojBmZTwMuZiCU/ds/OuWkNoqQa7pUgknBM5hCZF9QEDAwECAAkHAQAAAAAAAAA=",
            Base64.getEncoder().encodeToString(serializedTransaction)
        )
    }

    @Test
    fun mintTo_2tokens() {
        val instruction = TokenProgram.mintTo(
            mint = MINT_ACCOUNT_SIGNER.publicKey,
            destination = DESTINATION_KEY_PAIR.publicKey,
            mintAuthority = MINT_AUTHORITY_KEYPAIR_CboH.publicKey,
            amount = 2L,
        );

        val serializedTransaction = with(Transaction()) {
            addInstruction(instruction)
            setRecentBlockHash("Eit7RCyhUixAe2hGBS8oqnw59QK3kgMMjfLME5bm9wRn")
            sign(MINT_AUTHORITY_KEYPAIR_CboH)
            serialize()
        }

        assertEquals(
            "Aam3DFo+oKVjXARZbOPvmeMJatqm/mwV4I6a+RtsEzKaEEcbPtbPS5+kHVFImOrR55zypmD7+dJYD46RwBMoIA8BAAEErFqlrwrywtwjXsQB1O1cieb2oVvQ/GC+KL+QkD1OS5Z3MGxViNw4B26UTdl5FyefR+oQaPJ0EmrAJv76aOeg8PlBcgStmhLxhpL3FragO9vsGMIBV/pu+lpbifkTgF2vBt324ddloZPZy+FGzut5rBy0he1fWzeROoz1hX7/AKnL4ojBmZTwMuZiCU/ds/OuWkNoqQa7pUgknBM5hCZF9QEDAwECAAkHAgAAAAAAAAA=",
            Base64.getEncoder().encodeToString(serializedTransaction)
        )
    }

    @Test
    fun mintTo_99999999tokens() {
        val instruction = TokenProgram.mintTo(
            mint = MINT_ACCOUNT_SIGNER.publicKey,
            destination = DESTINATION_KEY_PAIR.publicKey,
            mintAuthority = MINT_AUTHORITY_KEYPAIR_CboH.publicKey,
            amount = 99999999L,
        );

        val serializedTransaction = with(Transaction()) {
            addInstruction(instruction)
            setRecentBlockHash("Eit7RCyhUixAe2hGBS8oqnw59QK3kgMMjfLME5bm9wRn")
            sign(MINT_AUTHORITY_KEYPAIR_CboH)
            serialize()
        }

        assertEquals(
            "AcvszODlzCObVWj9gSAE4zagap/N9tx0fYNLuKQowALJeW3oeWdNMnCstdnXkvjw50tQZxs74U8N6JEykNJlIwcBAAEErFqlrwrywtwjXsQB1O1cieb2oVvQ/GC+KL+QkD1OS5Z3MGxViNw4B26UTdl5FyefR+oQaPJ0EmrAJv76aOeg8PlBcgStmhLxhpL3FragO9vsGMIBV/pu+lpbifkTgF2vBt324ddloZPZy+FGzut5rBy0he1fWzeROoz1hX7/AKnL4ojBmZTwMuZiCU/ds/OuWkNoqQa7pUgknBM5hCZF9QEDAwECAAkH/+D1BQAAAAA=",
            Base64.getEncoder().encodeToString(serializedTransaction)
        )
    }

    companion object {
        // public key: 92GLpcVjbC1dA4TNRrb6ooNQGj7iqYk4bR1Xvwat2Wkf
        private val SECRET_KEY_92GL = intArrayOf(
            248, 245, 206, 215, 221, 248, 207, 125, 63, 204, 25, 40, 10, 180, 174,
            189, 124, 221, 111, 20, 34, 34, 147, 0, 194, 55, 115, 203, 41, 9, 128,
            188, 119, 48, 108, 85, 136, 220, 56, 7, 110, 148, 77, 217, 121, 23, 39,
            159, 71, 234, 16, 104, 242, 116, 18, 106, 192, 38, 254, 250, 104, 231,
            160, 240
        ).map(Int::toByte).toByteArray()
        private val MINT_ACCOUNT_SIGNER = HotAccount(SECRET_KEY_92GL)

        // HmzQ2Qy4UAJLyYBMuA56ErzXL5DAnNQzaMNTzGKPHdQA
        private val SECRET_KEY_HmzQ = intArrayOf(
            106, 19, 34, 63, 201, 43, 84, 254, 54, 99, 246, 106, 124, 215, 207, 247, 8,
            67, 209, 194, 163, 162, 206, 137, 82, 164, 170, 168, 247, 47, 224, 22, 249,
            65, 114, 4, 173, 154, 18, 241, 134, 146, 247, 22, 182, 160, 59, 219, 236,
            24, 194, 1, 87, 250, 110, 250, 90, 91, 137, 249, 19, 128, 93, 175
        ).map(Int::toByte).toByteArray()
        private val DESTINATION_KEY_PAIR = HotAccount(SECRET_KEY_HmzQ)

        // CboHzBWtkrx2a8NuJAT7yAQBcviKz944ihbufZAx3ZpH
        private val SECRET_KEY_CboH = intArrayOf(
            6, 211, 12, 186, 102, 70, 242, 127, 11, 26, 119, 11, 113, 161, 237, 218,
            129, 8, 225, 158, 67, 46, 30, 192, 80, 238, 47, 253, 109, 155, 170, 9, 172,
            90, 165, 175, 10, 242, 194, 220, 35, 94, 196, 1, 212, 237, 92, 137, 230,
            246, 161, 91, 208, 252, 96, 190, 40, 191, 144, 144, 61, 78, 75, 150
        ).map(Int::toByte).toByteArray()
        private val MINT_AUTHORITY_KEYPAIR_CboH = HotAccount(SECRET_KEY_CboH);

        private val MINT_AUTHORITY_PUBLIC_KEY_9Aq6 = PublicKey("9Aq6XkUT8Nx2ztkpkUxc4HiVCFWKTJZWiLnhC94iofvy")
        private val FREEZE_AUTHORITY = PublicKey("22BMtVRGveoYeJyvYDHfJP5JMCRKTYbp3QMhxU1P8w1n")
    }
}
