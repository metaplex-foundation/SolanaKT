package com.solana.networking.socket

import com.solana.core.PublicKeyJsonAdapter
import com.solana.models.buffer.moshi.AccountInfoJsonAdapter
import com.solana.models.buffer.moshi.MintJsonAdapter
import com.solana.models.buffer.moshi.TokenSwapInfoJsonAdapter
import com.solana.networking.RPCEndpoint
import com.solana.networking.models.RpcResponse
import com.solana.networking.socket.models.AccountNotification
import com.solana.networking.socket.models.ProgramNotification
import com.solana.networking.socket.models.SignatureNotification
import com.solana.networking.socket.models.TokenAccountNotificationData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.Executors

class MockSolanaLiveEventsDelegate: SolanaSocketEventsDelegate {
    var onConected: (() -> Void)? = null
    var onDisconnected: (() -> Void)? = null
    var onAccountNotification: (() -> Void)? = null
    var onSignatureNotification: (() -> Void)? = null
    var onLogsNotification: (() -> Void)? = null
    var onProgramNotification: (() -> Void)? = null
    var onSubscribed: ((Long, String) -> Void)? = null
    var onUnsubscribed: ((String) -> Void)? = null

    override fun connected() {
        onConected?.let { it() }
    }

    override fun accountNotification() {
        onAccountNotification?.let { it() }
    }

    override fun programNotification() {
        onProgramNotification?.let { it() }
    }

    override fun signatureNotification() {
        onSignatureNotification?.let { it() }
    }

    override fun logsNotification() {
        onLogsNotification?.let { it() }
    }

    override fun unsubscribed(id: String) {
        onUnsubscribed?.let { it(id) }
    }

    override fun subscribed(socketId: Long, id: String) {
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
    val moshi = Moshi.Builder().add(PublicKeyJsonAdapter()).addLast(KotlinJsonAdapterFactory()).build()

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

        val unSubscriptionAdapter: JsonAdapter<RpcResponse<AccountNotification<List<String>>>> = moshi.adapter(
            Types.newParameterizedType(
                RpcResponse::class.java,
                Types.newParameterizedType(
                    AccountNotification::class.java,
                    Types.newParameterizedType(
                        List::class.java,
                        String::class.java
                    )
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
        val unSubscriptionAdapter: JsonAdapter<RpcResponse<ProgramNotification<List<String>>>> = moshi.adapter(
            Types.newParameterizedType(
                RpcResponse::class.java,
                Types.newParameterizedType(
                    ProgramNotification::class.java,
                    Types.newParameterizedType(
                        List::class.java,
                        String::class.java
                    )
                )
            )
        )
        val result = unSubscriptionAdapter.fromJson(string)!!
        Assert.assertEquals(result.params?.subscription, 24040)
    }

    @Test
    fun testDecodingProgramNotification2() {
        val string = """
            {
               "jsonrpc":"2.0",
               "method":"programNotification",
               "params":{
                  "result":{
                     "context":{
                        "slot":67598736
                     },
                     "value":{
                        "account":{
                           "data":"nBuzaooPfhgHcAYxbpZVcXFw1EVjyEKxicgjr8u5NXLBX7xfCGw2E1YiSeeGXLbrKu5MAquX1zwR9P12vhAr1HgSXyTyR66VeevvJcyFKeEDSPWMzh723b8KLxtfd2TyPYYG5HYXx3HcH3Dbxvx17QxADJtRaHYTvde9pB98PsP9FcHWrzkCUZi4bhWtQYeUACGkYCQtMo2hbJuWqBG5rzS45rr9W2YJK",
                           "executable":false,
                           "lamports":2039280,
                           "owner":"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
                           "rentEpoch":156
                        },
                        "pubkey":"9FENcWRd1bf8P97e2exQtV3eEZCkaRa3KFFjTkYXpBHQ"
                     }
                  },
                  "subscription":22601084
               }
            }
        """.trimIndent()
        val unSubscriptionAdapter: JsonAdapter<RpcResponse<ProgramNotification<String>>> = moshi.adapter(
            Types.newParameterizedType(
                RpcResponse::class.java,
                Types.newParameterizedType(
                    ProgramNotification::class.java,
                    String::class.java
                )
            )
        )
        val result = unSubscriptionAdapter.fromJson(string)!!
        Assert.assertEquals(result.params?.subscription, 22601084)
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
        val unSubscriptionAdapter: JsonAdapter<RpcResponse<AccountNotification<TokenAccountNotificationData>>> = moshi.adapter(
            Types.newParameterizedType(
                RpcResponse::class.java,
                Types.newParameterizedType(
                    AccountNotification::class.java,
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