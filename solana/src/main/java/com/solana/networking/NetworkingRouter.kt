package com.solana.networking

import kotlinx.coroutines.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection

interface NetworkingRouter : JsonRpcDriver {
    val endpoint: RPCEndpoint
}

class HttpNetworkingRouter(
    override val endpoint: RPCEndpoint,
) : NetworkingRouter {

    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    override suspend fun <R> makeRequest(
        request: RpcRequestSerializable,
        resultSerializer: KSerializer<R>
    ): RpcResponseSerializable<R> =
        suspendCancellableCoroutine { continuation ->
            val url = endpoint.url
            with(url.openConnection() as HttpURLConnection) {
                // config
                setRequestProperty("Content-Type", "application/json; charset=utf-8")
                requestMethod = "POST"
                doOutput = true

                // cancellation
                continuation.invokeOnCancellation { disconnect() }

                // send request body
                outputStream.write(
                    json.encodeToString(RpcRequestSerializable.serializer(), request).toByteArray()
                )
                outputStream.close()

                // read response
                val responseString = inputStream.bufferedReader().use { it.readText() }

                // TODO: should check response code and/or errorStream for errors
                println("URL : $url")
                println("Response Code : $responseCode")
                println("input stream : $responseString")

                continuation.resumeWith(
                    Result.success(
                        json.decodeFromString(
                            RpcResponseSerializable.serializer(resultSerializer),
                            responseString
                        )
                    )
                )
            }
        }

}

