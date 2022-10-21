package com.solana.api

import com.solana.models.*
import com.solana.networking.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

class GetBlockRequest(
    slot: Int,
    encoding: RpcSendTransactionConfig.Encoding = RpcSendTransactionConfig.Encoding.base64,
    transactionDetails: String = "full",
    rewards: Boolean = true,
    commitment: String = "max",
    length: Int? = null,
    offset: Int? = length?.let { 0 }
) : RpcRequest() {
    override val method: String = "getBlock"
    override val params = buildJsonArray {
        add(slot)
        addJsonObject {
            put("encoding", encoding.getEncoding())
            put("transactionDetails", transactionDetails)
            put("rewards", rewards)
            put("commitment", commitment)
            length?.let {
                putJsonObject("dataSlice") {
                    put("length", length)
                    put("offset", offset)
                }
            }
        }
    }
}

@Serializable
data class ConfirmedTransactionSerializable(
    val meta: Meta?,
    val slot: Long? = null,
    val transaction: List<String>?
)

@Serializable
data class Header(
    val numReadonlySignedAccounts: Long,
    val numReadonlyUnsignedAccounts: Long,
    val numRequiredSignatures: Long
)

@Serializable
data class Instruction(
    val accounts: List<Long>? = null,
    val data: String? = null,
    val programIdIndex: Long? = null
)

@Serializable
data class Message(
    val accountKeys: List<String>,
    val header: Header,
    val instructions: List<Instruction>,
    val recentBlockhash: String
)

@Serializable
data class TokenAmountInfo(
    val amount: String?,
    val decimals: Int,
    val uiAmount: Double?,
    val uiAmountString: String
)

@Serializable
data class Status(
    val Ok: JsonObject? = null
)

@Serializable
data class TokenBalance(
    val accountIndex: Double,
    val mint: String,
    val uiTokenAmount: TokenAmountInfo
)

@Serializable
data class Meta(
    val err: JsonObject?,
    val fee: Long,
    val innerInstructions: List<Instruction>,
    val preTokenBalances: List<TokenBalance>,
    val postTokenBalances: List<TokenBalance>,
    val postBalances: List<Long>,
    val preBalances: List<Long>,
    val status: Status
)

@Serializable
data class Transaction(
    val message: Message,
    val signatures: List<String>,
)

@Serializable
enum class RewardType{
    Fee, Rent, Voting, Staking
}

@Serializable
data class Reward(
    val pubkey: String?,
    val lamports: Long,
    val postBalance: Long?,
    val rewardType: RewardType?,
)

@Serializable
data class Block(
    val blockTime: Long,
    val blockHeight: Long?,
    val blockhash: String?,
    val parentSlot: Long,
    val previousBlockhash: String?,
    val transactions: List<ConfirmedTransactionSerializable>? = null,
    val rewards: List<Reward>? = null
)

internal fun GetBlockSerializer() = Block.serializer()

fun Api.getBlock(slot: Int, onComplete: ((Result<Block>) -> Unit)) {
    CoroutineScope(dispatcher).launch {
        onComplete(getBlock(slot))
    }
}

suspend fun Api.getBlock(slot: Int): Result<Block> =
    router.makeRequestResult(GetBlockRequest(slot), GetBlockSerializer()).let { result ->
        @Suppress("UNCHECKED_CAST")
        if (result.isSuccess && result.getOrNull() == null)
            Result.failure(Error("Can not be null"))
        else result as Result<Block>// safe cast, null case handled above
    }