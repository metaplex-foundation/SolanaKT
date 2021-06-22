package com.solana.networking

import com.solana.networking.models.RpcRequest
import com.solana.networking.models.RpcResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.lang.reflect.Type

data class NetworkingError(override val message: String?) : Exception(message)

class NetworkingRouter(
    val endpoint: RPCEndpoint,
    private val httpClient: OkHttpClient = OkHttpClient()
) {
    companion object {
        private val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
    }

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    fun <T> call(
        method: String,
        params: List<Any>?,
        clazz: Class<T>?,
        onComplete: (Result<T>) -> Unit
    ) {

        val url = endpoint.url
        val rpcRequest = RpcRequest(method, params)
        val rpcRequestJsonAdapter: JsonAdapter<RpcRequest> =
            moshi.adapter(RpcRequest::class.java)
        val resultAdapter: JsonAdapter<RpcResponse<T>> = moshi
            .adapter(
                Types.newParameterizedType(
                    RpcResponse::class.java,
                    Type::class.java.cast(clazz)
                )
            )
        val request: Request = Request.Builder().url(url)
            .post(RequestBody.create(JSON, rpcRequestJsonAdapter.toJson(rpcRequest))).build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onComplete(Result.failure(RuntimeException(e)))
            }
            override fun onResponse(call: Call, response: Response) {
                try {
                    val responses = response.body!!.string()
                    val rpcResult: RpcResponse<T> =
                        resultAdapter.fromJson(responses)!!
                    if (rpcResult.error != null) {
                        onComplete(Result.failure(NetworkingError(rpcResult.error.message)))
                        return
                    } else {
                        onComplete(Result.success(rpcResult.result!!))
                        return
                    }
                } catch (e: Exception) {
                    onComplete(Result.failure(RuntimeException(e)))
                    return
                }
            }
        })
    }
}