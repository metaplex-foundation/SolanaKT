package com.solana.api

import com.solana.core.Account
import com.solana.core.Transaction
import com.solana.models.RpcSendTransactionConfig
import com.solana.networking.Commitment
import com.solana.networking.RpcRequestSerializable
import com.solana.networking.TransactionOptions
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.add
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.put
import org.bouncycastle.util.encoders.Base64

class SendTransactionRequest(serializedMessage: String,
                             skipPreflight: Boolean = false,
                             preflightCommitment: Commitment = Commitment.FINALIZED,
                             encoding: RpcSendTransactionConfig.Encoding = RpcSendTransactionConfig.Encoding.base64) : RpcRequestSerializable() {

    constructor(serializedMessage: String, transactionOptions: TransactionOptions) : this(
        serializedMessage,
        transactionOptions.skipPreflight,
        transactionOptions.preflightCommitment,
        transactionOptions.encoding
    )

    override val method = "sendTransaction"
    override val params = buildJsonArray {
        add(serializedMessage)
        addJsonObject {
            put("skipPreflight", skipPreflight)
            put("preflightCommitment", preflightCommitment.toString())
            put("encoding", encoding.getEncoding())
        }
    }
}

suspend fun Api.sendTransaction(
    transaction: Transaction,
    signers: List<Account>,
    recentBlockHash: String? = null
): Result<String> {
    val blockHash = recentBlockHash ?: getRecentBlockhash().getOrThrow()
    transaction.setRecentBlockHash(blockHash)
    transaction.sign(signers)
    val serialized = transaction.serialize()
    val base64Trx: String = Base64.toBase64String(serialized)

    return router.makeRequestResult(
        SendTransactionRequest(base64Trx),
        String.serializer()
    ).let { result ->
        @Suppress("UNCHECKED_CAST")
        if (result.isSuccess && result.getOrNull() == null)
            Result.failure(Error("Can not send transaction"))
        else result as Result<String> // safe cast, null case handled above
    }
}

fun Api.sendTransaction(
    transaction: Transaction,
    signers: List<Account>,
    recentBlockHash: String? = null,
    onComplete: ((Result<String>) -> Unit)
) {
    CoroutineScope(dispatcher).launch {
        onComplete(sendTransaction(transaction, signers, recentBlockHash))
    }
}

