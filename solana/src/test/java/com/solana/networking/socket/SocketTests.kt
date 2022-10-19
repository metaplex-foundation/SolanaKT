package com.solana.networking.socket

import com.solana.core.PublicKeyJsonAdapter
import com.solana.core.PublicKeyRule
import com.solana.models.ProgramAccount
import com.solana.models.buffer.*
import com.solana.models.buffer.moshi.AccountInfoJsonAdapter
import com.solana.models.buffer.moshi.MintJsonAdapter
import com.solana.models.buffer.moshi.TokenSwapInfoJsonAdapter
import com.solana.networking.RPCEndpoint
import com.solana.networking.models.RpcResponse
import com.solana.networking.socket.models.*
import com.solana.vendor.borshj.Borsh
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class MockSolanaLiveEventsDelegate: SolanaSocketEventsDelegate {
    var onConected: (() -> Unit)? = null
    var onDisconnected: (() -> Unit)? = null
    var onAccountNotification: ((RpcResponse<BufferInfo<AccountInfoData>>) -> Unit)? = null
    var onSignatureNotification: ((RpcResponse<SignatureNotification>) -> Unit)? = null
    var onLogsNotification: ((RpcResponse<LogsNotification>) -> Unit)? = null
    var onProgramNotification: ((RpcResponse<ProgramAccount<AccountInfoData>>) -> Unit)? = null
    var onSubscribed: ((Int, String) -> Unit)? = null
    var onUnsubscribed: ((String) -> Unit)? = null

    override fun connected() {
        onConected?.let { it() }
    }

    override fun accountNotification(notification: RpcResponse<BufferInfo<AccountInfoData>>) {
        onAccountNotification?.let { it(notification) }
    }

    override fun programNotification(notification: RpcResponse<ProgramAccount<AccountInfoData>>) {
        onProgramNotification?.let { it(notification) }
    }

    override fun signatureNotification(notification: RpcResponse<SignatureNotification>) {
        onSignatureNotification?.let { it(notification) }
    }

    override fun logsNotification(notification: RpcResponse<LogsNotification>) {
        onLogsNotification?.let { it(notification) }
    }

    override fun unsubscribed(id: String) {
        onUnsubscribed?.let { it(id) }
    }

    override fun subscribed(socketId: Int, id: String) {
        onSubscribed?.let { it(socketId, id) }
    }

    override fun disconnecting(code: Int, reason: String) {

    }

    override fun disconnected(code: Int, reason: String) {
        onDisconnected?.let { it() }
    }

    override fun error(error: Exception) {

    }
}

class SocketTests {
    val socket = SolanaSocket(RPCEndpoint.devnetSolana, enableDebugLogs = true)
    fun borsh(): Borsh {
        val borsh = Borsh()
        borsh.setRules(listOf(PublicKeyRule(), AccountInfoRule(), MintRule(), TokenSwapInfoRule()))
        return borsh
    }
    val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(PublicKeyJsonAdapter())
            .add(MintJsonAdapter(borsh()))
            .add(TokenSwapInfoJsonAdapter(borsh()))
            .add(AccountInfoJsonAdapter(borsh()))
            .addLast(KotlinJsonAdapterFactory()).build()
    }

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
            expected_id = socket.accountSubscribe("9B5XszUGdMaxCZ7uSQhPzdks5ZQSmWxrmzCSvtJ6Ns6g").getOrThrow()
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
            expected_id = socket.accountSubscribe("9B5XszUGdMaxCZ7uSQhPzdks5ZQSmWxrmzCSvtJ6Ns6g").getOrThrow()
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
            expected_id = socket.accountSubscribe("9B5XszUGdMaxCZ7uSQhPzdks5ZQSmWxrmzCSvtJ6Ns6g").getOrThrow()
        }

        delegate.onSubscribed = { socketId: Int, id: String ->
            latch.countDown()
            Assert.assertEquals(expected_id, id)
        }
        delegate.onAccountNotification = { notification ->
            latch.countDown()
            Assert.assertNotNull(notification.params?.result)
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
            expected_id = socket.logsSubscribe(listOf("9B5XszUGdMaxCZ7uSQhPzdks5ZQSmWxrmzCSvtJ6Ns6g")).getOrThrow()
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
            expected_id = socket.logsSubscribe(listOf("9B5XszUGdMaxCZ7uSQhPzdks5ZQSmWxrmzCSvtJ6Ns6g")).getOrThrow()
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
            expected_id = socket.logsSubscribe(listOf("9B5XszUGdMaxCZ7uSQhPzdks5ZQSmWxrmzCSvtJ6Ns6g")).getOrThrow()
        }

        delegate.onSubscribed = { socketId: Int, id: String ->
            latch.countDown()
            Assert.assertEquals(expected_id, id)
        }
        delegate.onLogsNotification = { notification ->
            latch.countDown()
            Assert.assertNotNull(notification.params?.result)
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
            expected_id = socket.programSubscribe("9B5XszUGdMaxCZ7uSQhPzdks5ZQSmWxrmzCSvtJ6Ns6g").getOrThrow()
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
            expected_id = socket.programSubscribe("9B5XszUGdMaxCZ7uSQhPzdks5ZQSmWxrmzCSvtJ6Ns6g").getOrThrow()
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
            expected_id = socket.programSubscribe("TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA").getOrThrow()
        }

        delegate.onSubscribed = { socketId: Int, id: String ->
            latch.countDown()
            Assert.assertEquals(expected_id, id)
        }
        delegate.onProgramNotification = { notification ->
            latch.countDown()
            Assert.assertNotNull(notification.params?.result)
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
            expected_id = socket.signatureSubscribe("Nfq1kEFqe5dBbTnprNZZVfnzvYJAKpUoibhYFBbaBXp37L7bAip89Qbs6mtiybQprY2GucMTgkxWPx81dNWh2Mh").getOrThrow()
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
            expected_id = socket.signatureSubscribe("Nfq1kEFqe5dBbTnprNZZVfnzvYJAKpUoibhYFBbaBXp37L7bAip89Qbs6mtiybQprY2GucMTgkxWPx81dNWh2Mh").getOrThrow()
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

    @Test
    fun testSocketSubscription() {
        val string = """
                {
                   "jsonrpc":"2.0",
                   "result":22529999,
                   "id":"ADFB8971-4473-4B16-A8BC-63EFD2F1FC8E"
                }
            """.trim()

        val unSubscriptionAdapter: JsonAdapter<RpcResponse<Int>> = moshi.adapter(
            Types.newParameterizedType(
                RpcResponse::class.java,
                Int::class.javaObjectType
            )
        )
        val result = unSubscriptionAdapter.fromJson(string)!!
        Assert.assertEquals(result.id, "ADFB8971-4473-4B16-A8BC-63EFD2F1FC8E")
        Assert.assertEquals(result.result, 22529999)
    }

    @Test
    fun testDecodingSOLAccountNotification() {
        val string = """
            {
                "jsonrpc":"2.0",
                "method":"accountNotification",
                "params":{
                   "result":{
                      "context":{
                         "slot":80221533
                      },
                      "value":{
                         "data":[
                            "",
                            "base64"
                         ],
                         "executable":false,
                         "lamports":41083620,
                         "owner":"11111111111111111111111111111111",
                         "rentEpoch":185
                      }
                   },
                   "subscription":46133
                }
             }
        """.trimIndent()

        val unSubscriptionAdapter: JsonAdapter<RpcResponse<BufferInfo<AccountInfoData>>> = moshi.adapter(
            Types.newParameterizedType(
                RpcResponse::class.java,
                Types.newParameterizedType(
                    BufferInfo::class.java,
                    AccountInfoData::class.java
                )
            )
        )
        val result = unSubscriptionAdapter.fromJson(string)!!
        Assert.assertEquals(result.params?.result?.value?.lamports, 41083620L)
    }

    @Test
    fun testDecodingProgramNotification() {
        val string = """
            {
               "jsonrpc":"2.0",
               "method":"programNotification",
               "params":{
                  "result":{
                     "context":{
                        "slot":5208469
                     },
                     "value":{
                        "pubkey":"H4vnBqifaSACnKa7acsxstsY1iV1bvJNxsCY7enrd1hq",
                        "account":{
                           "data":[
                              "11116bv5nS2h3y12kD1yUKeMZvGcKLSjQgX6BeV7u1FrjeJcKfsHPXHRDEHrBesJhZyqnnq9qJeUuF7WHxiuLuL5twc38w2TXNLxnDbjmuR",
                              "base58"
                           ],
                           "executable":false,
                           "lamports":33594,
                           "owner":"11111111111111111111111111111111",
                           "rentEpoch":636
                        }
                     }
                  },
                  "subscription":24040
               }
            }
        """.trimIndent()
        val unSubscriptionAdapter: JsonAdapter<RpcResponse<ProgramAccount<AccountInfoData>>> = moshi.adapter(
            Types.newParameterizedType(
                RpcResponse::class.java,
                Types.newParameterizedType(
                    ProgramAccount::class.java,
                    AccountInfoData::class.java
                )
            )
        )
        val result = unSubscriptionAdapter.fromJson(string)!!
        Assert.assertEquals(result.params?.subscription, 24040)
    }

    @Test
    fun testDecodingTokenAccountNotification() {
        val string = """
        {
           "jsonrpc":"2.0",
           "method":"accountNotification",
           "params":{
              "result":{
                 "context":{
                    "slot":80216037
                 },
                 "value":{
                    "data":{
                       "parsed":{
                          "info":{
                             "isNative":false,
                             "mint":"kinXdEcpDQeHPEuQnqmUgtYykqKGVFq6CeVX5iAHJq6",
                             "owner":"6QuXb6mB6WmRASP2y8AavXh6aabBXEH5ZzrSH5xRrgSm",
                             "state":"initialized",
                             "tokenAmount":{
                                "amount":"390000101",
                                "decimals":5,
                                "uiAmount":3900.00101,
                                "uiAmountString":"3900.00101"
                             }
                          },
                          "type":"account"
                       },
                       "program":"spl-token",
                       "space":165
                    },
                    "executable":false,
                    "lamports":2039280,
                    "owner":"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
                    "rentEpoch":185
                 }
              },
              "subscription":42765
           }
        }
        """.trimIndent()
        val unSubscriptionAdapter: JsonAdapter<RpcResponse<BufferInfoJson<TokenAccountNotificationData>>> = moshi.adapter(
            Types.newParameterizedType(
                RpcResponse::class.java,
                Types.newParameterizedType(
                    BufferInfoJson::class.java,
                    TokenAccountNotificationData::class.java
                )
            )
        )
        val result = unSubscriptionAdapter.fromJson(string)!!
        Assert.assertEquals(result.params?.subscription, 42765)
    }

    @Test
    fun testDecodingSignatureNotification() {
        val string = """
            {
               "jsonrpc":"2.0",
               "method":"signatureNotification",
               "params":{
                  "result":{
                     "context":{
                        "slot":80768508
                     },
                     "value":{
                        "err":null
                     }
                  },
                  "subscription":43601
               }
            }
            """.trimIndent()
        val unSubscriptionAdapter: JsonAdapter<RpcResponse<SignatureNotification>> = moshi.adapter(
            Types.newParameterizedType(
                RpcResponse::class.java,
                SignatureNotification::class.java
            )
        )
        val result = unSubscriptionAdapter.fromJson(string)!!
        Assert.assertEquals(result.params?.subscription, 43601)
    }
}