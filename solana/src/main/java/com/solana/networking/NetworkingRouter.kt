package com.solana.networking

import com.solana.core.PublicKeyJsonAdapter
import com.solana.core.PublicKeyRule
import com.solana.models.buffer.AccountInfoRule
import com.solana.models.buffer.MintRule
import com.solana.models.buffer.TokenSwapInfoRule
import com.solana.models.buffer.moshi.AccountInfoJsonAdapter
import com.solana.models.buffer.moshi.MintJsonAdapter
import com.solana.models.buffer.moshi.TokenSwapInfoJsonAdapter
import com.solana.networking.models.RPCError
import com.solana.networking.models.RpcRequest
import com.solana.networking.models.RpcResponse
import com.solana.vendor.borshj.Borsh
import com.solana.vendor.borshj.BorshRule
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.lang.reflect.Type

sealed class NetworkingError(override val message: String?) : Exception(message) {
    object invalidResponseNoData : NetworkingError("No data was returned.")
    data class invalidResponse(val rpcError: RPCError) : NetworkingError(rpcError.message)
    data class decodingError(val rpcError: java.lang.Exception) : NetworkingError(rpcError.message)
}

class NetworkingRouterConfig(val rules: List<BorshRule<*>> = listOf(), val moshiAdapters: List<Object> = listOf())

class NetworkingRouter(
    val endpoint: RPCEndpoint,
    private val httpClient: OkHttpClient = OkHttpClient(),
    private val config: NetworkingRouterConfig? = null
) {

    private fun borsh(): Borsh {
        val borsh = Borsh()
        val rules = listOf(PublicKeyRule(), AccountInfoRule(), MintRule(), TokenSwapInfoRule()) + (config?.rules ?: listOf())
        borsh.setRules(rules)
        return borsh
    }

    private val moshi: Moshi by lazy {
        val moshiBuilder = Moshi.Builder()
            .add(PublicKeyJsonAdapter())
            .add(MintJsonAdapter(borsh()))
            .add(TokenSwapInfoJsonAdapter(borsh()))
            .add(AccountInfoJsonAdapter(borsh()))

        for (adapter in config?.moshiAdapters ?: listOf()) {
            moshiBuilder.add(adapter)
        }

        moshiBuilder.addLast(KotlinJsonAdapterFactory())
        moshiBuilder.build()
    }

    companion object {
        private val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
    }

    fun <T> request(
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
                        }
                }.run {
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
}

