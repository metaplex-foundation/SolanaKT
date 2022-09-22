package com.solana.networking

import com.solana.core.PublicKeyRule
import com.solana.models.buffer.AccountInfoRule
import com.solana.models.buffer.MintRule
import com.solana.models.buffer.TokenSwapInfoRule
import com.solana.models.buffer.moshi.AccountInfoJsonAdapter
import com.solana.models.buffer.moshi.MintJsonAdapter
import com.solana.models.buffer.moshi.TokenSwapInfoJsonAdapter
import com.solana.networking.models.*
import com.solana.vendor.borshj.Borsh
import com.solana.vendor.borshj.BorshRule
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.lang.reflect.Type
import java.net.HttpURLConnection

sealed class NetworkingError(override val message: String?) : Exception(message) {
    object invalidResponseNoData : NetworkingError("No data was returned.")
    data class invalidResponse(val rpcError: RPCError) : NetworkingError(rpcError.message)
    data class decodingError(val rpcError: java.lang.Exception) : NetworkingError(rpcError.message)
}

interface MoshiAdapterFactory {
    fun create(borsh: Borsh): Object
}

class NetworkingRouterConfig(
    val rules: List<BorshRule<*>> = listOf(),
    val moshiAdapters: List<MoshiAdapterFactory> = listOf()
)

interface NetworkingRouter : JsonRpcDriver {
    val endpoint: RPCEndpoint
    fun <T> request(
        method: String,
        params: List<Any>?,
        clazz: Type?,
        onComplete: (Result<T>) -> Unit
    )
}

class OkHttpNetworkingRouter(
    override val endpoint: RPCEndpoint,
    private val httpClient: OkHttpClient = OkHttpClient(),
    private val config: NetworkingRouterConfig? = null
) : NetworkingRouter {

    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    private fun borsh(): Borsh {
        val borsh = Borsh()
        val rules = listOf(
            PublicKeyRule(),
            AccountInfoRule(),
            MintRule(),
            TokenSwapInfoRule()
        ) + (config?.rules ?: listOf())
        borsh.setRules(rules)
        return borsh
    }

    private val moshi: Moshi by lazy {
        val moshiBuilder = Moshi.Builder()
            .add(MintJsonAdapter(borsh()))
            .add(TokenSwapInfoJsonAdapter(borsh()))
            .add(AccountInfoJsonAdapter(borsh()))

        for (adapter in config?.moshiAdapters ?: listOf()) {
            moshiBuilder.add(adapter.create(borsh()))
        }

        moshiBuilder.addLast(KotlinJsonAdapterFactory())
        moshiBuilder.build()
    }

    companion object {
        private val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
    }

    override fun <T> request(
        method: String,
        params: List<Any>?,
        clazz: Type?,
        onComplete: (Result<T>) -> Unit
    ) {
        val url = endpoint.url
        val rpcRequest = RpcRequest(method, params)
        val rpcRequestJsonAdapter: JsonAdapter<RpcRequest> = moshi.adapter(RpcRequest::class.java)

        val jsonParams = rpcRequestJsonAdapter.toJson(rpcRequest)
        val request: Request = Request.Builder().url(url)
            .post(RequestBody.create(JSON, jsonParams)).build()

        call(request, clazz, onComplete)
    }

    private fun <T> call(
        request: Request,
        clazz: Type?,
        onComplete: (Result<T>) -> Unit
    ) {
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onComplete(Result.failure(RuntimeException(e)))
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { body ->
                    val responses = body.string()
                    fromJsonToResult<T>(responses, clazz)
                        .map { rpcResult ->
                            rpcResult.error?.let { error ->
                                onComplete(Result.failure(NetworkingError.invalidResponse(error)))
                                return
                            }
                            rpcResult.result?.let { result ->
                                onComplete(Result.success(result))
                                return
                            }
                        }.onFailure {
                            onComplete(Result.failure(it))
                            return
                        }
                } ?: run {
                    onComplete(Result.failure(NetworkingError.invalidResponseNoData))
                }
            }
        })
    }

    private fun <T> fromJsonToResult(string: String, clazz: Type?): Result<RpcResponse<T>> {
        return try {
            val adapter: JsonAdapter<RpcResponse<T>> = moshi.adapter(
                Types.newParameterizedType(
                    RpcResponse::class.java,
                    clazz
                )
            )
            val result = adapter.fromJson(string)!!
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(NetworkingError.decodingError(e))
        }
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

