package com.solana.core

import com.solana.programs.MemoProgram.writeUtf8
import com.solana.programs.SystemProgram
import org.bitcoinj.core.Base58
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*


class TransactionTest {
    @Test
    fun signAndSerialize() {
        val fromPublicKey = PublicKey("QqCCvshxtqMAL2CVALqiJB7uEeE5mjSPsseQdDzsRUo")
        val toPublickKey = PublicKey("GrDMoeqMLFjeXQ24H56S1RLgT4R76jsuWCd6SvXyGPQ5")
        val lamports = 3000
        val transaction = Transaction()
        transaction.addInstruction(SystemProgram.transfer(fromPublicKey, toPublickKey, lamports.toLong()))
        transaction.setRecentBlockHash("Eit7RCyhUixAe2hGBS8oqnw59QK3kgMMjfLME5bm9wRn")
        transaction.sign(signer)
        val serializedTransaction: ByteArray = transaction.serialize(verifySignatures = false).getOrThrows()

        println(transaction)

        assertEquals(
            "ASdDdWBaKXVRA+6flVFiZokic9gK0+r1JWgwGg/GJAkLSreYrGF4rbTCXNJvyut6K6hupJtm72GztLbWNmRF1Q4BAAEDBhrZ0FOHFUhTft4+JhhJo9+3/QL6vHWyI8jkatuFPQzrerzQ2HXrwm2hsYGjM5s+8qMWlbt6vbxngnO8rc3lqgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAy+KIwZmU8DLmYglP3bPzrlpDaKkGu6VIJJwTOYQmRfUBAgIAAQwCAAAAuAsAAAAAAAA=",
            Base64.getEncoder().encodeToString(serializedTransaction)
        )
    }

    @Test
    fun transactionBuilderTest() {
        val memo = "Test memo"
        val transaction: Transaction = TransactionBuilder()
            .addInstruction(
                writeUtf8(
                    signer.publicKey,
                    memo
                )
            )
            .setRecentBlockHash("Eit7RCyhUixAe2hGBS8oqnw59QK3kgMMjfLME5bm9wRn")
            .setSigners(listOf(signer))
            .build()
        assertEquals(
            "AV6w4Af9PSHhNsTSal4vlPF7Su9QXgCVyfDChHImJITLcS5BlNotKFeMoGw87VwjS3eNA2JCL+MEoReynCNbWAoBAAECBhrZ0FOHFUhTft4+JhhJo9+3/QL6vHWyI8jkatuFPQwFSlNQ+F3IgtYUpVZyeIopbd8eq6vQpgZ4iEky9O72oMviiMGZlPAy5mIJT92z865aQ2ipBrulSCScEzmEJkX1AQEBAAlUZXN0IG1lbW8=",
            Base64.getEncoder().encodeToString(transaction.serialize().getOrThrows())
        )
    }

    companion object {
        private val signer: Account = Account(
            Base58
                .decode("4Z7cXSyeFR8wNGMVXUE1TwtKn5D5Vu7FzEv69dokLv7KrQk7h6pu4LF8ZRR9yQBhc7uSM6RTTZtU1fmaxiNrxXrs")
        )
    }
}
