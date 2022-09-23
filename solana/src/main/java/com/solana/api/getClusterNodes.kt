package com.solana.api

import com.solana.core.PublicKey
import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import com.solana.networking.serialization.serializers.solana.PublicKeyAsStringSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

class GetClusterNodesRequest : RpcRequestSerializable() {
    override val method: String = "getClusterNodes"
}

@Serializable
data class ClusterNode(
    @Serializable(with = PublicKeyAsStringSerializer::class) var pubkey: PublicKey,
    var gossip: String?,
    var tpu: String?,
    var rpc: String?,
    var version: String?
)

internal fun GetClusterNodesSerializer() = ListSerializer(ClusterNode.serializer())

fun Api.getClusterNodes(onComplete: (Result<List<ClusterNode>>) -> Unit) {
    CoroutineScope(dispatcher).launch {
        onComplete(getClusterNodes())
    }
}

suspend fun Api.getClusterNodes(): Result<List<ClusterNode>> =
    router.makeRequestResult(GetClusterNodesRequest(), GetClusterNodesSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<List<ClusterNode>> // safe cast, null case handled above
        }