package com.solana.networking

import com.solana.networking.models.RpcRequest
import com.solana.networking.models.RpcResponse
import com.solana.api.RpcException
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.lang.reflect.Type

class NetworkingRouter(val endpoint: RPCEndpoint, private val httpClient: OkHttpClient = OkHttpClient()) {
    companion object{
        private val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
    }

    @Throws(RpcException::class)
    fun <T> call(method: String, params: List<Any>?, clazz: Class<T>?): T {
        val url = endpoint.url
        val rpcRequest = RpcRequest(method, params)
        val rpcRequestJsonAdapter: JsonAdapter<RpcRequest> =
            Moshi.Builder().build().adapter( RpcRequest::class.java )
        val resultAdapter: JsonAdapter<RpcResponse<T>> = Moshi.Builder().build()
            .adapter(
                Types.newParameterizedType(
                    RpcResponse::class.java,
                    Type::class.java.cast(clazz)
                )
            )
        val request: Request = Request.Builder().url(url)
            .post(RequestBody.create(JSON, rpcRequestJsonAdapter.toJson(rpcRequest))).build()
        return try {
            val response: Response = httpClient.newCall(request).execute()
            val rpcResult: RpcResponse<T> = resultAdapter.fromJson(response.body!!.string())!!
            if (rpcResult.error != null) {
                throw RpcException(rpcResult.error.message)
            }
            rpcResult.result!!
        } catch (e: IOException) {
            throw RpcException(e.message)
        }
    }
}