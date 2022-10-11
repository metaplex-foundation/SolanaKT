package com.solana.api

import com.solana.core.PublicKey
import com.solana.networking.RpcRequestSerializable
import com.solana.networking.SolanaResponseSerializer
import com.solana.networking.makeRequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray

class GetTokenAccountBalanceRequest(tokenAccount: PublicKey) : RpcRequestSerializable() {
    override val method: String = "getTokenAccountBalance"
    override val params = buildJsonArray {
        add(tokenAccount.toString())
    }
}

@Serializable
data class TokenAmountInfoResponse(
    val amount: String?,
    val decimals: Int,
    val uiAmount: Double?,
    val uiAmountString: String
)

internal fun GetTokenAccountBalanceSerializer() = SolanaResponseSerializer(TokenAmountInfoResponse.serializer())

suspend fun Api.getTokenAccountBalance(tokenAccount: PublicKey): Result<TokenAmountInfoResponse> =
    router.makeRequestResult(GetTokenAccountBalanceRequest(tokenAccount), GetTokenAccountBalanceSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<TokenAmountInfoResponse> // safe cast, null case handled above
        }

fun Api.getTokenAccountBalance(tokenAccount: PublicKey,
                           onComplete: (Result<TokenAmountInfoResponse>) -> Unit)  {

    CoroutineScope(dispatcher).launch {
        onComplete(getTokenAccountBalance(tokenAccount))
    }
}