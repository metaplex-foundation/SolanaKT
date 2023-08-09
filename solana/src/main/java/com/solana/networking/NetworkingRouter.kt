package com.solana.networking

import kotlinx.coroutines.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.lang.Exception
import java.net.HttpURLConnection
import kotlin.coroutines.resumeWithException

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
        request: RpcRequest,
        resultSerializer: KSerializer<R>
    ): RpcResponse<R> =
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
                    json.encodeToString(RpcRequest.serializer(), request).toByteArray()
                )
                outputStream.close()

                when (responseCode) {
                    HttpURLConnection.HTTP_OK -> {
                        try {
                            val responseString = inputStream.bufferedReader().use { it.readText() }

                            val decoded = json.decodeFromString(
                                RpcResponse.serializer(resultSerializer), responseString)
                            continuation.resumeWith(
                                Result.success(decoded)
                            )
                        } catch (e: SerializationException){
                            continuation.resumeWithException(e)
                        }
                    }
                    else -> {
                        val errorString = errorStream.bufferedReader().use { it.readText() }
                        continuation.resumeWithException(Exception(errorString))
                    }
                }
            }
        }

}

