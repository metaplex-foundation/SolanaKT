package com.solana.models

import com.solana.core.*
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import java.lang.Byte
import java.util.Base64

class TransactionTest {
    @Test
    fun `compile message - accountKeys are ordered`() {
        // These pubkeys are chosen specially to be in sort order.
        val payer = PublicKey(
            "3qMLYYyNvaxNZP7nW8u5abHMoJthYqQehRLbFVPNNcvQ",
        )
        val accountWritableSigner2 = PublicKey(
            "3XLtLo5Z4DG8b6PteJidF6kFPNDfxWjxv4vTLrjaHTvd",
        )
        val accountWritableSigner3 = PublicKey(
            "4rvqGPb4sXgyUKQcvmPxnWEZTTiTqNUZ2jjnw7atKVxa",
        )
        val accountSigner4 = PublicKey(
            "5oGjWjyoKDoXGpboGBfqm9a5ZscyAjRi3xuGYYu1ayQg",
        )
        val accountSigner5 = PublicKey(
            "65Rkc3VmDEV6zTRGtgdwkTcQUxDJnJszj2s4WoXazYpC",
        )
        val accountWritable6 = PublicKey(
            "72BxBZ9eD9Ue6zoJ9bzfit7MuaDAnq1qhirgAoFUXz9q",
        )
        val accountWritable7 = PublicKey(
            "BtYrPUeVphVgRHJkf2bKz8DLRxJdQmZyANrTM12xFqZL",
        )
        val accountRegular8 = PublicKey(
            "Di1MbqFwpodKzNrkjGaUHhXC4TJ1SHUAxo9agPZphNH1",
        )
        val accountRegular9 = PublicKey(
            "DYzzsfHTgaNhCgn7wMaciAYuwYsGqtVNg9PeFZhH93Pc",
        )
        val programId = PublicKey(
            "Fx9svCTdxnACvmEmx672v2kP1or4G1zC73tH7XsXbKkP",
        )

        val recentBlockhash = HotAccount().publicKey.toBase58()

        val transaction = Transaction()
        transaction.recentBlockhash = recentBlockhash
        transaction.add(
            TransactionInstruction(
                programId, listOf(
                    // Regular accounts
                    AccountMeta(accountRegular9, false, false),
                    AccountMeta(accountRegular8, false, false),
                    // Writable accounts
                    AccountMeta(accountWritable7, false, true),
                    AccountMeta(accountWritable6, false, true),
                    // Signers
                    AccountMeta(accountSigner5, true, false),
                    AccountMeta(accountSigner4, true, false),
                    // Writable Signers
                    AccountMeta(accountWritableSigner3, true, true),
                    AccountMeta(accountWritableSigner2, true, true),
                    // Payer
                    AccountMeta(payer, true, true),
                )
            )
        )

        transaction.feePayer = payer

        val message = transaction.compileMessage()
        // Payer comes first.
        assertEquals(message.accountKeys[0], payer)

        // Writable signers come next, in pubkey order.
        assertEquals(message.accountKeys[1], accountWritableSigner2)
        assertEquals(message.accountKeys[2], accountWritableSigner3)

        // Signers come next, in pubkey order.
        assertEquals(message.accountKeys[3], accountSigner4)
        assertEquals(message.accountKeys[4], accountSigner5)

        // Writable accounts come next, in pubkey order.
        assertEquals(message.accountKeys[5], accountWritable6)
        assertEquals(message.accountKeys[6], accountWritable7)

        // Everything else afterward, in pubkey order.
        assertEquals(message.accountKeys[7], accountRegular8)
        assertEquals(message.accountKeys[8], accountRegular9)
        assertEquals(message.accountKeys[9], programId)
    }

    @Test
    fun `compile message - accountKeys collapses signedness and writability of duplicate accounts`() {
        // These pubkeys are chosen specially to be in sort order.
        val payer = PublicKey(
            "2eBgaMN8dCnCjx8B8Wrwk974v5WHwA6Vvj4N2mW9KDyt",
        )
        val account2 = PublicKey(
            "DL8FErokCN7rerLdmJ7tQvsL1FsqDu1sTKLLooWmChiW",
        )
        val account3 = PublicKey(
            "EdPiTYbXFxNrn1vqD7ZdDyauRKG4hMR6wY54RU1YFP2e",
        )
        val account4 = PublicKey(
            "FThXbyKK4kYJBngSSuvo9e6kc7mwPHEgw4V8qdmz1h3k",
        )
        val programId = PublicKey(
            "Gcatgv533efD1z2knsH9UKtkrjRWCZGi12f8MjNaDzmN",
        )
        val account5 = PublicKey(
            "rBtwG4bx85Exjr9cgoupvP1c7VTe7u5B36rzCg1HYgi",
        )

        val recentBlockhash = HotAccount().publicKey.toBase58()

        val transaction = Transaction()
        transaction.recentBlockhash = recentBlockhash
        transaction.add(
            TransactionInstruction(
                programId, listOf(
                    // Should sort last.
                    AccountMeta(account5, false, false),
                    AccountMeta(account5, false, false),
                    // Should be considered writeable.
                    AccountMeta(account4, false, true),
                    AccountMeta(account4, false, true),
                    // Should be considered a signer.
                    AccountMeta(account3, true, false),
                    AccountMeta(account3, true, false),
                    // Should be considered a writable signer.
                    AccountMeta(account2, true, true),
                    AccountMeta(account2, true, true),
                    // Payer
                    AccountMeta(payer, true, true),
                )
            )
        )

        transaction.feePayer = payer

        val message = transaction.compileMessage()
        // Payer comes first.
        assertEquals(message.accountKeys[0], payer)

        // Writable signer comes first.
        assertEquals(message.accountKeys[1], account2)

        // Signer comes next.
        assertEquals(message.accountKeys[2], account3)

        // Writable account comes next.
        assertEquals(message.accountKeys[3], account4)

        // Regular accounts come last.
        assertEquals(message.accountKeys[4], programId)
        assertEquals(message.accountKeys[5], account5)
    }

    @Test
    fun `compile message - payer is first account meta`() {
        val payer = HotAccount()
        val other = HotAccount()
        val recentBlockhash = HotAccount().publicKey.toBase58()
        val programId = HotAccount().publicKey

        val transaction = Transaction()
        transaction.recentBlockhash = recentBlockhash
        transaction.add(
            TransactionInstruction(
                programId, listOf(
                    // Should sort last.
                    AccountMeta(other.publicKey, true, true),
                    AccountMeta(payer.publicKey, true, true),
                )
            )
        )

        transaction.sign(payer, other)
        val message = transaction.compileMessage()

        assertEquals(message.accountKeys[0], payer.publicKey)
        assertEquals(message.accountKeys[1], other.publicKey)

        assertEquals(message.header.numRequiredSignatures, Byte(2))
        assertEquals(message.header.numReadonlySignedAccounts, Byte(0))
        assertEquals(message.header.numReadonlyUnsignedAccounts, Byte(1))
    }
}