package com.solana.api

import com.solana.core.PublicKey
import com.solana.networking.RpcRequest
import com.solana.networking.makeRequestResult
import com.solana.networking.serialization.serializers.solana.PublicKeyAsStringSerializer
import com.solana.networking.serialization.serializers.solana.SolanaResponseSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray

class GetTokenLargestAccountsRequest(tokenMint: PublicKey) : RpcRequest() {
    override val method: String = "getTokenLargestAccounts"
    override val params = buildJsonArray {
        add(tokenMint.toString())
    }
}

@Serializable
data class TokenAccount (
    val amount: String?,
    val decimals: Int,
    val uiAmount: Double?,
    val uiAmountString: String?,
    @Serializable(with = PublicKeyAsStringSerializer::class) val address: PublicKey?
)

internal fun GetTokenLargestAccountsSerializer() = SolanaResponseSerializer(ListSerializer(TokenAccount.serializer()))

suspend fun Api.getTokenLargestAccounts(tokenMint: PublicKey): Result<List<TokenAccount>> =
    router.makeRequestResult(GetTokenLargestAccountsRequest(tokenMint), GetTokenLargestAccountsSerializer())
        .let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Can not be null"))
            else result as Result<List<TokenAccount>> // safe cast, null case handled above
        }

fun Api.getTokenLargestAccounts(
    tokenMint: PublicKey,
    onComplete: (Result<List<TokenAccount>>) -> Unit
) {
    CoroutineScope(dispatcher).launch {
        onComplete(getTokenLargestAccounts(tokenMint))
    }
}