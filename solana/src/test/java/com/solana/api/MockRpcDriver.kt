package com.solana.api

import com.solana.networking.*
import kotlinx.serialization.KSerializer
import java.lang.Error
import java.lang.reflect.Type


class MockRpcDriver(override val endpoint: RPCEndpoint = RPCEndpoint.devnetSolana) :
    NetworkingRouter {

    val willReturn = mutableMapOf<RpcRequestSerializable, Any>()
    val willError = mutableMapOf<RpcRequestSerializable, RpcError>()

    inline fun <reified R : Any> willReturn(forRequest: RpcRequestSerializable, willReturn: R) {
        this.willReturn[forRequest] = willReturn
    }

    fun willError(forRequest: RpcRequestSerializable, willError: RpcError) {
        this.willError[forRequest] = willError
    }

    override fun <T> request(
        method: String,
        params: List<Any>?,
        clazz: Type?,
        onComplete: (Result<T>) -> Unit
    ) {
        throw Error("Not yet implemented")
    }

    override suspend fun <R> makeRequest(
        request: RpcRequestSerializable,
        resultSerializer: KSerializer<R>
    ): RpcResponseSerializable<R> {
        findErrorForRequest(request)?.let { error ->
            return RpcResponseSerializable(error = RpcError(error.code, error.message))
        }
        return RpcResponseSerializable(findReturnForRequest(request) as R)
    }

    private fun findErrorForRequest(request: RpcRequestSerializable): RpcError? = willError[
            willError.keys.find {
                it.method == request.method && it.params == request.params
            }
    ]

    private fun findReturnForRequest(request: RpcRequestSerializable): Any? = willReturn[
            willReturn.keys.find {
                it.method == request.method && it.params == request.params
            }
    ]
}