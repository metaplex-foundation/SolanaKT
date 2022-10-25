package com.solana.api

import com.solana.core.PublicKey
import com.solana.networking.*
import com.solana.networking.SolanaResponseSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

class SimulateTransactionRequest(
    transaction: String,
    simulateTransactionConfig: SimulateTransactionConfig,
) : RpcRequest() {
    override val method: String = "simulateTransaction"
    override val params = buildJsonArray {
        add(transaction)
        addJsonObject {
            put("encoding", simulateTransactionConfig.encoding.getEncoding())
            put("commitment", simulateTransactionConfig.commitment.value)
            put("sigVerify", simulateTransactionConfig.sigVerify)
            put("replaceRecentBlockhash", simulateTransactionConfig.replaceRecentBlockhash)
            simulateTransactionConfig.accounts?.let { map ->
                putJsonObject("accounts"){
                    put("encoding", map["encoding"] as String)
                    putJsonArray("addresses"){
                        (map["addresses"] as List<String>).forEach { address ->
                            add(address)
                        }
                    }
                }
            }
        }
    }
}

data class SimulateTransactionConfig (
    var encoding: Encoding = Encoding.base64,
    var accounts: Map<String, *>? = null,
    val commitment: Commitment = Commitment.FINALIZED,
    val sigVerify: Boolean = false,
    var replaceRecentBlockhash: Boolean = false
)

@Serializable
data class SimulateTransactionValue (
    val accounts: List<SimulatedAccountValue>,
    val logs: List<String>,
)

@Serializable
data class SimulatedAccountValue (
    val data: List<String>,
    val executable:Boolean,
    val lamports: Long,
    val owner: String?,
    val rentEpoch: Long,
)

internal fun SimulateTransactionSerializer() = SolanaResponseSerializer(SimulateTransactionValue.serializer())

suspend fun Api.simulateTransaction(
    transaction: String,
    addresses: List<PublicKey>
): Result<SimulateTransactionValue> {
    val simulateTransactionConfig =
        SimulateTransactionConfig(Encoding.base64)
    val base58addresses = addresses.map(PublicKey::toBase58)
    val accounts = mapOf(
        "encoding" to Encoding.base64.getEncoding(),
        "addresses" to base58addresses)
    simulateTransactionConfig.accounts = accounts
    simulateTransactionConfig.replaceRecentBlockhash = true


    return router.makeRequestResult(SimulateTransactionRequest(
        transaction = transaction,
        simulateTransactionConfig = simulateTransactionConfig
    ), SimulateTransactionSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<SimulateTransactionValue> // safe cast, null case handled above
        }
}

fun Api.simulateTransaction(
    transaction: String,
    addresses: List<PublicKey>,
    onComplete: (Result<SimulateTransactionValue>) -> Unit
) {
    CoroutineScope(dispatcher).launch {
        onComplete(simulateTransaction(transaction, addresses))
    }
}