package com.solana.rxsolana

import com.solana.rxsolana.api.getRecentBlockhash
import com.solana.rxsolana.api.getConfirmedTransaction
import com.solana.rxsolana.api.sendTransaction
import com.solana.rxsolana.api.getBalance
import com.solana.rxsolana.api.getVoteAccounts
import com.solana.Solana
import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.networking.NetworkingRouter
import com.solana.networking.RPCEndpoint
import com.solana.programs.MemoProgram
import com.solana.programs.SystemProgram
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert
import org.junit.Test
import java.util.*

class Methods {
    @Test
    fun TestGetRecentBlockhash() {
        val logging = HttpLoggingInterceptor()
        logging.level = (HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(logging).build()
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getRecentBlockhash().blockingGet()
        Assert.assertNotNull(result)
    }
    @Test
    fun TestGetBalance() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getBalance(PublicKey("AaXs7cLGcSVAsEt8QxstVrqhLhYN2iGhFNRemwYnHitV")).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetConfirmedTransaction() {
        val logging = HttpLoggingInterceptor()
        logging.level = (HttpLoggingInterceptor.Level.BODY)
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getConfirmedTransaction("7Zk9yyJCXHapoKyHwd8AzPeW9fJWCvszR6VAcHUhvitN5W9QG9JRnoYXR8SBQPTh27piWEmdybchDt5j7xxoUth").blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetVoteAccounts() {
        val logging = HttpLoggingInterceptor()
        logging.level = (HttpLoggingInterceptor.Level.BODY)
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getVoteAccounts().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun transactionMemoTest() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))

        val lamports = 10101
        val destination: PublicKey = PublicKey("3h1zGmCwsRJnVk5BuRNMLsPaQu1y2aqXqXDWYCgrp5UG")

        // Create account from private key
        val feePayer: Account = Account.fromMnemonic(
            Arrays.asList(
                "hint", "begin", "crowd", "dolphin", "drive", "render", "finger", "above", "sponsor", "prize", "runway", "invest", "dizzy", "pony", "bitter", "trial", "ignore", "crop", "please", "industry", "hockey", "wire", "use", "side"
            ), ""
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

        val result = solana.api.sendTransaction(transaction, feePayer).blockingGet()
        Assert.assertNotNull(result)
    }
}