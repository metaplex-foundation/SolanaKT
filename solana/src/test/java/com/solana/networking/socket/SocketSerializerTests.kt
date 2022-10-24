package com.solana.networking.socket

import com.solana.api.AccountInfo
import com.solana.api.ProgramAccountSerialized
import com.solana.models.buffer.*
import com.solana.networking.RpcResponse
import com.solana.networking.serialization.serializers.base58.BorshAsBase58JsonArraySerializer
import com.solana.networking.serialization.serializers.base64.BorshAsBase64JsonArraySerializer
import com.solana.networking.socket.models.*
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import org.junit.Assert
import org.junit.Test

class SocketSerializerTests {

    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
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

        val serializer = SocketResponse.serializer(Int.serializer())

        val result = json.decodeFromString(serializer, string)
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

        val serializer = SocketResponse.serializer(
            AccountInfo.serializer(BorshAsBase64JsonArraySerializer(AccountInfoData.serializer()))
        )

        val result = json.decodeFromString(serializer, string)
        Assert.assertEquals(result.params?.result?.value?.lamports, 41083620L)
        Assert.assertEquals(result.params?.result?.context?.slot, 80221533L)
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

        val serializer = SocketResponse.serializer(
            ProgramAccountSerialized.serializer(
                AccountInfo.serializer(
                    BorshAsBase58JsonArraySerializer(String.serializer().nullable))
                )
            )

        val result = json.decodeFromString(serializer, string)
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

        val serializer =
            SocketResponse.serializer(
                AccountInfo.serializer(
                    TokenAccountNotificationData.serializer()
                )
            )
        val result = json.decodeFromString(serializer, string)
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

        val serializer = SocketResponse.serializer(SignatureNotification.serializer())

        val result = json.decodeFromString(serializer, string)

        Assert.assertEquals(result.params?.subscription, 43601)
        Assert.assertEquals(result.params?.result!!.context!!.slot, 80768508)
        Assert.assertEquals(result.params?.result!!.value!!.err, null)
    }
}