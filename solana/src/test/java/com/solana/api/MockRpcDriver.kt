package com.solana.api

import com.solana.networking.*
import kotlinx.serialization.KSerializer


class MockRpcDriver(override val endpoint: RPCEndpoint = RPCEndpoint.devnetSolana) :
    NetworkingRouter {

    val willReturn = mutableMapOf<RpcRequest, Any>()
    val willError = mutableMapOf<RpcRequest, RpcError>()

    inline fun <reified R : Any> willReturn(forRequest: RpcRequest, willReturn: R) {
        this.willReturn[forRequest] = willReturn
    }

    fun willError(forRequest: RpcRequest, willError: RpcError) {
        this.willError[forRequest] = willError
    }

    override suspend fun <R> makeRequest(
        request: RpcRequest,
        resultSerializer: KSerializer<R>
    ): RpcResponse<R> {
        findErrorForRequest(request)?.let { error ->
            return RpcResponse(error = RpcError(error.code, error.message))
        }
        return RpcResponse(findReturnForRequest(request) as R)
    }

    private fun findErrorForRequest(request: RpcRequest): RpcError? = willError[
            willError.keys.find {
                it.method == request.method && it.params == request.params
            }
    ]

    private fun findReturnForRequest(request: RpcRequest): Any? = willReturn[
            willReturn.keys.find {
                it.method == request.method && it.params == request.params
            }
    ]
}