package com.solana.networking.socket

import com.solana.api.AccountInfo
import com.solana.api.ProgramAccountSerialized
import com.solana.models.buffer.*
import com.solana.networking.RPCEndpoint
import com.solana.networking.RpcResponseSerializable
import com.solana.networking.socket.models.*
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class MockSolanaLiveEventsDelegate : SolanaSocketEventsDelegate {
    var onConected:  (() -> Unit)? = null
    var onDisconnected: (() -> Unit)? = null
    var onAccountNotification: ((RpcResponseSerializable<AccountInfo<AccountInfoData?>>) -> Unit)? =
        null
    var onSignatureNotification: ((RpcResponseSerializable<SignatureNotification>) -> Unit)? = null
    var onLogsNotification: ((RpcResponseSerializable<LogsNotification>) -> Unit)? = null
    var onProgramNotification: ((RpcResponseSerializable<ProgramAccountSerialized<AccountInfo<AccountInfoData?>>>) -> Unit)? =
        null
    var onSubscribed: ((Int, String) -> Unit)? = null
    var onUnsubscribed: ((String) -> Unit)? = null

    override fun connected() {
        onConected?.let { it() }
    }

    override fun accountNotification(notification: RpcResponseSerializable<AccountInfo<AccountInfoData?>>) {
        onAccountNotification?.let { it(notification) }
    }

    override fun programNotification(notification: RpcResponseSerializable<ProgramAccountSerialized<AccountInfo<AccountInfoData?>>>) {
        onProgramNotification?.let { it(notification) }
    }

    override fun signatureNotification(notification: RpcResponseSerializable<SignatureNotification>) {
        onSignatureNotification?.let { it(notification) }
    }

    override fun logsNotification(notification: RpcResponseSerializable<LogsNotification>) {
        onLogsNotification?.let { it(notification) }
    }

    override fun unsubscribed(id: String) {
        onUnsubscribed?.let { it(id) }
    }

    override fun subscribed(socketId: Int, id: String) {
        onSubscribed?.let { it(socketId, id) }
    }

    override fun disconnecting(code: Int, reason: String) {
        onDisconnected?.let { it() }
    }

    override fun disconnected(code: Int, reason: String) {
        onDisconnected?.let { it() }
    }

    override fun error(error: Exception) {

    }
}

class SocketTests {
    val socket = SolanaSocket(RPCEndpoint.devnetSolana, enableDebugLogs = true)

    @Test
    fun testSocketConnected() {
        val delegate = MockSolanaLiveEventsDelegate()
        val latch = CountDownLatch(1)
        delegate.onConected = {
            Assert.assertTrue(true)
            latch.countDown()
        }
        socket.start(delegate)
        latch.await(20, TimeUnit.SECONDS)
    }

    @Test
    fun testSocketAccountSubscribe() {
        val latch = CountDownLatch(2)
        val delegate = MockSolanaLiveEventsDelegate()
        var expected_id: String?
        delegate.onConected = {
            latch.countDown()
            expected_id =
                socket.accountSubscribe("9B5XszUGdMaxCZ7uSQhPzdks5ZQSmWxrmzCSvtJ6Ns6g").getOrThrow()
        }
        delegate.onSubscribed = { socketId: Int, id: String ->
            latch.countDown()
            Assert.assertEquals(id, id)
            socket.stop()
        }
        socket.start(delegate)
        latch.await(20, TimeUnit.SECONDS)
    }

    @Test
    fun testSocketAccountUnSubscribe() {
        val latch = CountDownLatch(3)
        val delegate = MockSolanaLiveEventsDelegate()
        var expected_id: String?
        delegate.onConected = {
            latch.countDown()
            expected_id =
                socket.accountSubscribe("9B5XszUGdMaxCZ7uSQhPzdks5ZQSmWxrmzCSvtJ6Ns6g").getOrThrow()
        }
        delegate.onSubscribed = { socketId: Int, id: String ->
            latch.countDown()
            socket.accountUnSubscribe(socketId)
            Assert.assertEquals(id, id)
        }
        delegate.onUnsubscribed = { id: String ->
            latch.countDown()
            Assert.assertNotNull(id)
            socket.stop()
        }
        socket.start(delegate)
        latch.await(20, TimeUnit.SECONDS)
    }

    @Test
    fun testSocketAccountNotification() {
        val latch = CountDownLatch(3)
        val delegate = MockSolanaLiveEventsDelegate()
        var expected_id: String? = null
        delegate.onConected = {
            latch.countDown()
            expected_id =
                socket.accountSubscribe("9B5XszUGdMaxCZ7uSQhPzdks5ZQSmWxrmzCSvtJ6Ns6g").getOrThrow()
        }

        delegate.onSubscribed = { socketId: Int, id: String ->
            latch.countDown()
            Assert.assertEquals(expected_id, id)
        }
        delegate.onAccountNotification = { notification ->
            latch.countDown()
            Assert.assertNotNull(notification.result)
            socket.stop()
        }
        socket.start(delegate)
        latch.await(20, TimeUnit.SECONDS)
    }

    @Test
    fun testSocketLogsSubscribe() {
        val latch = CountDownLatch(2)
        val delegate = MockSolanaLiveEventsDelegate()
        var expected_id: String?
        delegate.onConected = {
            latch.countDown()
            expected_id =
                socket.logsSubscribe(listOf("9B5XszUGdMaxCZ7uSQhPzdks5ZQSmWxrmzCSvtJ6Ns6g"))
                    .getOrThrow()
        }
        delegate.onSubscribed = { socketId: Int, id: String ->
            latch.countDown()
            Assert.assertEquals(id, id)
            socket.stop()
        }
        socket.start(delegate)
        latch.await(20, TimeUnit.SECONDS)
    }

    @Test
    fun testSocketLogsUnSubscribe() {
        val latch = CountDownLatch(3)
        val delegate = MockSolanaLiveEventsDelegate()
        var expected_id: String?
        delegate.onConected = {
            latch.countDown()
            expected_id =
                socket.logsSubscribe(listOf("9B5XszUGdMaxCZ7uSQhPzdks5ZQSmWxrmzCSvtJ6Ns6g"))
                    .getOrThrow()
        }
        delegate.onSubscribed = { socketId: Int, id: String ->
            latch.countDown()
            socket.logsUnsubscribe(socketId)
            Assert.assertEquals(id, id)
        }
        delegate.onUnsubscribed = { id: String ->
            latch.countDown()
            Assert.assertNotNull(id)
            socket.stop()
        }
        socket.start(delegate)
        latch.await(20, TimeUnit.SECONDS)
    }

    @Test
    fun testSocketLogsNotification() {
        val latch = CountDownLatch(3)
        val delegate = MockSolanaLiveEventsDelegate()
        var expected_id: String? = null
        delegate.onConected = {
            latch.countDown()
            expected_id =
                socket.logsSubscribe(listOf("9B5XszUGdMaxCZ7uSQhPzdks5ZQSmWxrmzCSvtJ6Ns6g"))
                    .getOrThrow()
        }

        delegate.onSubscribed = { socketId: Int, id: String ->
            latch.countDown()
            Assert.assertEquals(expected_id, id)
        }
        delegate.onLogsNotification = { notification ->
            latch.countDown()
            Assert.assertNotNull(notification.result)
            socket.stop()
        }
        socket.start(delegate)
        latch.await(20, TimeUnit.SECONDS)
    }

    @Test
    fun testSocketProgramSubscribe() {
        val latch = CountDownLatch(2)
        val delegate = MockSolanaLiveEventsDelegate()
        var expected_id: String?
        delegate.onConected = {
            latch.countDown()
            expected_id =
                socket.programSubscribe("9B5XszUGdMaxCZ7uSQhPzdks5ZQSmWxrmzCSvtJ6Ns6g").getOrThrow()
        }
        delegate.onSubscribed = { socketId: Int, id: String ->
            latch.countDown()
            Assert.assertEquals(id, id)
            socket.stop()
        }
        socket.start(delegate)
        latch.await(20, TimeUnit.SECONDS)
    }

    @Test
    fun testSocketProgramUnSubscribe() {
        val latch = CountDownLatch(3)
        val delegate = MockSolanaLiveEventsDelegate()
        var expected_id: String?
        delegate.onConected = {
            latch.countDown()
            expected_id =
                socket.programSubscribe("9B5XszUGdMaxCZ7uSQhPzdks5ZQSmWxrmzCSvtJ6Ns6g").getOrThrow()
        }
        delegate.onSubscribed = { socketId: Int, id: String ->
            latch.countDown()
            socket.programUnsubscribe(socketId)
            Assert.assertEquals(id, id)
        }
        delegate.onUnsubscribed = { id: String ->
            latch.countDown()
            Assert.assertNotNull(id)
            socket.stop()
        }
        socket.start(delegate)
        latch.await(20, TimeUnit.SECONDS)
    }

    @Test
    fun testSocketProgramNotification() {
        val latch = CountDownLatch(3)
        val delegate = MockSolanaLiveEventsDelegate()
        var expected_id: String? = null
        delegate.onConected = {
            latch.countDown()
            expected_id =
                socket.programSubscribe("TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA").getOrThrow()
        }

        delegate.onSubscribed = { socketId: Int, id: String ->
            latch.countDown()
            Assert.assertEquals(expected_id, id)
        }
        delegate.onProgramNotification = { notification ->
            latch.countDown()
            Assert.assertNotNull(notification.result)
            socket.stop()
        }
        socket.start(delegate)
        latch.await(20, TimeUnit.SECONDS)
    }

    @Test
    fun testSocketSignatureSubscribe() {
        val latch = CountDownLatch(2)
        val delegate = MockSolanaLiveEventsDelegate()
        var expected_id: String?
        delegate.onConected = {
            latch.countDown()
            expected_id =
                socket.signatureSubscribe("Nfq1kEFqe5dBbTnprNZZVfnzvYJAKpUoibhYFBbaBXp37L7bAip89Qbs6mtiybQprY2GucMTgkxWPx81dNWh2Mh")
                    .getOrThrow()
        }
        delegate.onSubscribed = { socketId: Int, id: String ->
            latch.countDown()
            Assert.assertEquals(id, id)
            socket.stop()
        }
        socket.start(delegate)
        latch.await(20, TimeUnit.SECONDS)
    }

    @Test
    fun testSocketSignatureUnSubscribe() {
        val latch = CountDownLatch(3)
        val delegate = MockSolanaLiveEventsDelegate()
        var expected_id: String?
        delegate.onConected = {
            latch.countDown()
            expected_id =
                socket.signatureSubscribe("Nfq1kEFqe5dBbTnprNZZVfnzvYJAKpUoibhYFBbaBXp37L7bAip89Qbs6mtiybQprY2GucMTgkxWPx81dNWh2Mh")
                    .getOrThrow()
        }
        delegate.onSubscribed = { socketId: Int, id: String ->
            latch.countDown()
            socket.signatureUnsubscribe(socketId)
            Assert.assertEquals(id, id)
        }
        delegate.onUnsubscribed = { id: String ->
            latch.countDown()
            Assert.assertNotNull(id)
            socket.stop()
        }
        socket.start(delegate)
        latch.await(20, TimeUnit.SECONDS)
    }
}