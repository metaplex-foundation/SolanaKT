/*
 * JsonRpcDriver
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.solana.networking

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonElement

/*
 * Trying out a new pattern here where the native interface supports serialization, but we also
 * provide a method overload that hides the serialization complexity through an extension method.
 * This way we can provide a more 'traditional' service layer api while simultaneously supporting
 * more advanced usage with custom response objects and/or serializers
 */

/**
 * JsonRpcDriver
 *
 * Makes a standard JSON-RPC 2.0 request and returns a [RpcResponse]
 *
 * @author Funkatronics
 */
interface JsonRpcDriver {

    /**
     * Performs the [request] and returns the resulting [RpcResponse]
     */
    suspend fun <R> makeRequest(
        request: RpcRequestSerializable,
        resultSerializer: KSerializer<R>
    ): RpcResponseSerializable<R>

}

suspend inline fun <reified R> JsonRpcDriver.makeRequestResult(
    request: RpcRequestSerializable,
    serializer: KSerializer<R>
): Result<R?> =
    this.makeRequest(request, serializer).let { response ->
        (response.result)?.let { result ->
            return Result.success(result)
        }

        response.error?.let {
            return Result.failure(Error(it.message))
        }

        // an empty error and empty result means we did not find anything, return null
        return Result.success(null)
    }


suspend fun JsonRpcDriver.makeRequest(request: RpcRequestSerializable): DefaultRpcResponse =
    makeRequest(request, JsonElement.serializer())