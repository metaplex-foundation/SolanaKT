package com.solana.actions

import com.solana.Solana
import com.solana.api.SimulateTransactionValue
import com.solana.api.requestAirdrop
import com.solana.api.sendTransaction
import com.solana.api.simulateTransaction
import com.solana.core.DerivationPath
import com.solana.core.HotAccount
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.networking.HttpNetworkingRouter
import com.solana.networking.RPCEndpoint
import com.solana.programs.MemoProgram
import com.solana.programs.SystemProgram
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class ActionsTests {
    lateinit var sender: HotAccount
    val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))

    @Test
    fun TestSendSOL() = runTest {
        sender = HotAccount.fromMnemonic(listOf(
            "hint", "begin", "crowd", "dolphin", "drive", "render", "finger", "above", "sponsor", "prize", "runway", "invest", "dizzy", "pony", "bitter", "trial", "ignore", "crop", "please", "industry", "hockey", "wire", "use", "side"
        ), "")

        val result = solana.action.sendSOL(
            sender,
            PublicKey("3h1zGmCwsRJnVk5BuRNMLsPaQu1y2aqXqXDWYCgrp5UG"),
            1
        ).getOrThrow()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetTokenWallets()= runTest {
        val result = solana.action.getTokenWallets(
            PublicKey("3h1zGmCwsRJnVk5BuRNMLsPaQu1y2aqXqXDWYCgrp5UG"),
        ).getOrThrow()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestCreateTokenAccount()= runTest {
        val sender: HotAccount = HotAccount.fromMnemonic(
            Arrays.asList(
                "siege", "amazing", "camp", "income", "refuse", "struggle", "feed", "kingdom", "lawn", "champion", "velvet", "crystal", "stomach", "trend", "hen", "uncover", "roast", "nasty", "until", "hidden", "crumble", "city", "bag", "minute"
            ), ""
            , DerivationPath.BIP44_M_44H_501H_0H_OH
        )
        val result = solana.action.createTokenAccount(sender, PublicKey("6AUM4fSvCAxCugrbJPFxTqYFp9r3axYx973yoSyzDYVH")).getOrThrow()
        Assert.assertNotNull(result)
    }

    // This works but requires a 0 balance wallet
    /*@Test
    fun TestCloseTokenAccount() {
        val sender: Account = Account.fromMnemonic(
            Arrays.asList(
                "siege", "amazing", "camp", "income", "refuse", "struggle", "feed", "kingdom", "lawn", "champion", "velvet", "crystal", "stomach", "trend", "hen", "uncover", "roast", "nasty", "until", "hidden", "crumble", "city", "bag", "minute"
            ), ""
            , DerivationPath.BIP44_M_44H_501H_0H_OH
        )
        val auth = InMemoryAccountStorage()
        auth.save(sender)
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana), auth)
        val result = solana.action.closeTokenAccount(sender, PublicKey("FRo1fyKnzeNTbk22ZSjwUXQLceqSos7et3h2uqNLpUar")).blockingGet()
        Assert.assertNotNull(result)
    }*/

    @Test
    fun simulateTransactionTest() = runTest {
        val transaction =
            "ASdDdWBaKXVRA+6flVFiZokic9gK0+r1JWgwGg/GJAkLSreYrGF4rbTCXNJvyut6K6hupJtm72GztLbWNmRF1Q4BAAEDBhrZ0FOHFUhTft4+JhhJo9+3/QL6vHWyI8jkatuFPQzrerzQ2HXrwm2hsYGjM5s+8qMWlbt6vbxngnO8rc3lqgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAy+KIwZmU8DLmYglP3bPzrlpDaKkGu6VIJJwTOYQmRfUBAgIAAQwCAAAAuAsAAAAAAAA="
        val addresses = listOf(PublicKey.valueOf("QqCCvshxtqMAL2CVALqiJB7uEeE5mjSPsseQdDzsRUo"))
        val simulatedTransaction: SimulateTransactionValue =
            solana.api.simulateTransaction(transaction, addresses).getOrThrow()
        Assert.assertTrue(simulatedTransaction.logs.isNotEmpty())
    }

    @Test
    fun sendTransactionTests() = runTest {

        val lamports = 111L
        val destination = PublicKey("3h1zGmCwsRJnVk5BuRNMLsPaQu1y2aqXqXDWYCgrp5UG")

        val feePayer: HotAccount = HotAccount.fromMnemonic(
            Arrays.asList(
                "hint", "begin", "crowd", "dolphin", "drive", "render", "finger", "above", "sponsor", "prize", "runway", "invest", "dizzy", "pony", "bitter", "trial", "ignore", "crop", "please", "industry", "hockey", "wire", "use", "side"
            ), ""
            , DerivationPath.BIP44_M_44H_501H_0H_OH
        )

        val instructions = SystemProgram.transfer(feePayer.publicKey, destination, lamports)
        val transaction = Transaction()
        transaction.addInstruction(instructions)
        val result = solana.api.sendTransaction(transaction, listOf(feePayer)).getOrThrow()
        Assert.assertNotNull(result)
    }

    @Test
    fun transactionMemoTest() = runTest {

        val lamports = 10101
        val destination = PublicKey("3h1zGmCwsRJnVk5BuRNMLsPaQu1y2aqXqXDWYCgrp5UG")

        // Create account from private key
        val feePayer: HotAccount = HotAccount.fromMnemonic(
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

        val result = solana.api.sendTransaction(transaction, listOf(feePayer)).getOrThrow()
        Assert.assertNotNull(result)
    }

    @Test
    fun getMintDataTest() = runTest {
        val result = solana.action.getMintData(PublicKey("8wzZaGf89zqx7PRBoxk9T6QyWWQbhwhdU555ZxRnceG3")).getOrThrow()
        Assert.assertNotNull(result)
    }

    @Test
    fun getMultipleMintDatas() = runTest {
        val result =
            solana.action.getMultipleMintDatas(listOf(PublicKey("8wzZaGf89zqx7PRBoxk9T6QyWWQbhwhdU555ZxRnceG3")))
                .getOrThrow()
        Assert.assertNotNull(result)
    }

    @Test
    fun sendSPLTokensTest() = runTest {

        // Create account from private key
        val feePayer: HotAccount = HotAccount.fromMnemonic(
            Arrays.asList(
                "siege", "amazing", "camp", "income", "refuse", "struggle", "feed", "kingdom", "lawn", "champion", "velvet", "crystal", "stomach", "trend", "hen", "uncover", "roast", "nasty", "until", "hidden", "crumble", "city", "bag", "minute"
            ), ""
            , DerivationPath.BIP44_M_44H_501H_0H_OH
        )
        val mintAddress = PublicKey("6AUM4fSvCAxCugrbJPFxTqYFp9r3axYx973yoSyzDYVH")
        val source =  PublicKey("8hoBQbSFKfDK3Mo7Wwc15Pp2bbkYuJE8TdQmnHNDjXoQ")
        val destination =  PublicKey("8hoBQbSFKfDK3Mo7Wwc15Pp2bbkYuJE8TdQmnHNDjXoQ")
        val transactionId = solana.action.sendSPLTokens(
            mintAddress = mintAddress,
            fromPublicKey = source,
            destinationAddress = destination,
            amount = 1,
            account = feePayer,
        ).getOrThrow()
        Assert.assertNotNull(transactionId)
    }

    @Test
    fun sendSPLTokensUnfundedAccountTest() = runTest {

        // Create account from private key
        val feePayer: HotAccount = HotAccount.fromMnemonic(
            Arrays.asList(
                "siege", "amazing", "camp", "income", "refuse", "struggle", "feed", "kingdom", "lawn", "champion", "velvet", "crystal", "stomach", "trend", "hen", "uncover", "roast", "nasty", "until", "hidden", "crumble", "city", "bag", "minute"
            ), ""
            , DerivationPath.BIP44_M_44H_501H_0H_OH
        )
        val mintAddress = PublicKey("6AUM4fSvCAxCugrbJPFxTqYFp9r3axYx973yoSyzDYVH")
        val source =  PublicKey("8hoBQbSFKfDK3Mo7Wwc15Pp2bbkYuJE8TdQmnHNDjXoQ")
        val destination =  HotAccount().publicKey
        val transactionId = solana.action.sendSPLTokens(
            mintAddress = mintAddress,
            fromPublicKey = source,
            destinationAddress = destination,
            allowUnfundedRecipient = true,
            amount = 1,
            account = feePayer,
        ).getOrThrow()
        Assert.assertNotNull(transactionId)
    }
}