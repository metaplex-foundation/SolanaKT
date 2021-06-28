package com.solana.rxsolana.actions

import com.solana.Solana
import com.solana.core.Account
import com.solana.core.DerivationPath
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.models.SimulatedTransaction
import com.solana.networking.NetworkingRouter
import com.solana.networking.RPCEndpoint
import com.solana.programs.MemoProgram
import com.solana.programs.SystemProgram
import com.solana.rxsolana.api.*
import org.junit.Assert
import org.junit.Test
import java.util.*


class Action {
    @Test
    fun TestSendSOL() {
        val sender: Account = Account.fromMnemonic(listOf(
            "hint", "begin", "crowd", "dolphin", "drive", "render", "finger", "above", "sponsor", "prize", "runway", "invest", "dizzy", "pony", "bitter", "trial", "ignore", "crop", "please", "industry", "hockey", "wire", "use", "side"
        ), "")
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.action.sendSOL(
            sender,
            PublicKey("3h1zGmCwsRJnVk5BuRNMLsPaQu1y2aqXqXDWYCgrp5UG"),
            100000
        ).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestCreateTokenAccount() {
        val sender: Account = Account.fromMnemonic(listOf(
            "hint", "begin", "crowd", "dolphin", "drive", "render", "finger", "above", "sponsor", "prize", "runway", "invest", "dizzy", "pony", "bitter", "trial", "ignore", "crop", "please", "industry", "hockey", "wire", "use", "side"
        ), "")
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.action.createTokenAccount(sender, PublicKey("6AUM4fSvCAxCugrbJPFxTqYFp9r3axYx973yoSyzDYVH")).blockingGet()
        Assert.assertNotNull(result)
    }


    @Test
    fun simulateTransactionTest() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val transaction =
            "ASdDdWBaKXVRA+6flVFiZokic9gK0+r1JWgwGg/GJAkLSreYrGF4rbTCXNJvyut6K6hupJtm72GztLbWNmRF1Q4BAAEDBhrZ0FOHFUhTft4+JhhJo9+3/QL6vHWyI8jkatuFPQzrerzQ2HXrwm2hsYGjM5s+8qMWlbt6vbxngnO8rc3lqgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAy+KIwZmU8DLmYglP3bPzrlpDaKkGu6VIJJwTOYQmRfUBAgIAAQwCAAAAuAsAAAAAAAA="
        val addresses = listOf(PublicKey.valueOf("QqCCvshxtqMAL2CVALqiJB7uEeE5mjSPsseQdDzsRUo"))
        val simulatedTransaction: SimulatedTransaction =
            solana.api.simulateTransaction(transaction, addresses).blockingGet()
        Assert.assertTrue(simulatedTransaction.value.logs.size > 0)
    }

    @Test
    fun transactionMemoTest() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))

        val lamports = 10101
        val destination = PublicKey("3h1zGmCwsRJnVk5BuRNMLsPaQu1y2aqXqXDWYCgrp5UG")

        // Create account from private key
        val feePayer: Account = Account.fromMnemonic(
            Arrays.asList(
                "hint", "begin", "crowd", "dolphin", "drive", "render", "finger", "above", "sponsor", "prize", "runway", "invest", "dizzy", "pony", "bitter", "trial", "ignore", "crop", "please", "industry", "hockey", "wire", "use", "side"
            ), ""
            , DerivationPath.BIP44_M_44H_501H_0H_OH
        )
        val transaction = Transaction()
        transaction.addInstruction(
            SystemProgram.transfer(
                feePayer.publicKey,
                destination,
                lamports.toLong()
            )
        )

        // Add instruction to write memo
        transaction.addInstruction(
            MemoProgram.writeUtf8(feePayer.publicKey, "Hello from tests :)")
        )

        val result = solana.api.sendTransaction(transaction, listOf(feePayer)).blockingGet()
        Assert.assertNotNull(result)
    }
}