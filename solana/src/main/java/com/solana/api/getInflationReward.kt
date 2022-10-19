package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.RpcEpochConfig
import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.json.*

class GetInflationRewardRequest(val addresses: List<PublicKey>,
                                val epoch: Long? = null,
                                val commitment: String? = null) : RpcRequestSerializable() {
    override val method: String = "getInflationReward"
    override val params = buildJsonArray {
        addJsonArray {
            addresses.map(PublicKey::toString).forEach {
                add(it)
            }
        }
        epoch?.let {
            addJsonObject {
                put("epoch", it)
                put("commitment", commitment)
            }
        }
    }
}

@Serializable
data class InflationRewardResponse(
    val epoch: Double,
    val effectiveSlot: Double,
    val amount: Double,
    val postBalance: Double
)

internal fun GetInflationRewardSerializer() = ListSerializer(InflationRewardResponse.serializer().nullable)

suspend fun Api.getInflationReward(
    addresses: List<PublicKey>,
    epoch: Long? = null,
    commitment: String? = null
): Result<List<InflationRewardResponse>> =
    router.makeRequestResult(GetInflationRewardRequest(addresses, epoch, commitment), GetInflationRewardSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<List<InflationRewardResponse>> // safe cast, null case handled above
        }


fun Api.getInflationReward(
    addresses: List<PublicKey>,
    epoch: Long? = null,
    commitment: String? = null,
    onComplete: (Result<List<InflationRewardResponse>>) -> Unit
) {
    val params: MutableList<Any> = ArrayList()
    params.add(addresses.map(PublicKey::toString))

    epoch?.let {
        params.add(RpcEpochConfig(it, commitment))
    }

    CoroutineScope(dispatcher).launch {
        onComplete(getInflationReward(addresses, epoch, commitment))
    }
}