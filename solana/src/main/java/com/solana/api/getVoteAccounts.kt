package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.VoteAccountConfig
import com.solana.networking.RpcRequestSerializable
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

class GetVoteAccountsRequest(votePubkey: PublicKey? = null) : RpcRequestSerializable() {
    override val method: String = "getVoteAccounts"
    override val params = buildJsonArray {
        addJsonObject {
            votePubkey?.let {
                put("votePubkey", votePubkey.toBase58())
            }
        }
    }
}

@Serializable
data class VoteAccounts(
    val current: List<VoteAccount>,
    val delinquent: List<VoteAccount>,
)

@Serializable
data class VoteAccount (
    val commission: Long,
    val epochVoteAccount: Boolean,
    val epochCredits: List<List<Long>>,
    val nodePubkey: String,
    val lastVote: Long,
    val activatedStake: Long,
    val votePubkey: String?
)

internal fun GetVoteAccountsSerializer() = VoteAccounts.serializer()

fun Api.getVoteAccounts(votePubkey: PublicKey? = null, onComplete: ((Result<VoteAccounts>) -> Unit)) {
    CoroutineScope(dispatcher).launch {
        onComplete(getVoteAccounts(votePubkey))
    }
}

suspend fun Api.getVoteAccounts(votePubkey: PublicKey? = null): Result<VoteAccounts> =
    router.makeRequestResult(GetVoteAccountsRequest(votePubkey), GetVoteAccountsSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<VoteAccounts> // safe cast, null case handled above
        }