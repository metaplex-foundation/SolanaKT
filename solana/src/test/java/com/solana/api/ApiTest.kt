package com.solana.api

import com.solana.core.Account
import com.solana.core.HotAccount
import com.solana.core.Transaction
import com.solana.networking.OkHttpNetworkingRouter
import com.solana.networking.RPCEndpoint
import com.solana.programs.SystemProgram
import org.bitcoinj.crypto.MnemonicCode
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ApiTest {
    // Mnemonic generate via the generateMnemonicCode() - account is prefunded so we don't have to await for airdrop
    // on every test but the airdrop itself will ensure we have enough credit for next runs
    private val fundedAccount = HotAccount.fromMnemonic(
        listOf(
            "wine",
            "visa",
            "multiply",
            "involve",
            "play",
            "canoe",
            "wisdom",
            "illegal",
            "city",
            "language",
            "worth",
            "erosion"
        ), ""
    )
    private val api = Api(OkHttpNetworkingRouter(RPCEndpoint.devnetSolana))

    @Before
    fun setup() {
//        generateMnemonicCode()
        fundAccount(fundedAccount, api)
    }

    private fun generateMnemonicCode() {
        val b = ByteArray(16) // 128 bits is 12 seed words
        Random().nextBytes(b)
        println(MnemonicCode.INSTANCE.toMnemonic(b))
    }

    @Test
    fun shouldSendTransaction() {
        val destinationAccount = HotAccount()
        val countDownLatch = CountDownLatch(1)

        val instructions =
            SystemProgram.transfer(fundedAccount.publicKey, destinationAccount.publicKey, 1_000_000)
        val transaction = Transaction()
        transaction.add(instructions)

        api.sendTransaction(transaction, listOf(fundedAccount)) {
            assertTrue(it.isSuccess)
            countDownLatch.countDown()
        }

        assertTrue(countDownLatch.await(5L, TimeUnit.SECONDS))
    }

    private fun fundAccount(account: Account, api: Api) {
        api.requestAirdrop(account.publicKey, 1_000_000_000) {
            it.onSuccess {
                println("Airdrop transaction id is $it")
            }
        }
    }
}