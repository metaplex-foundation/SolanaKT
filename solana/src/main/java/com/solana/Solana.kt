package com.solana

import com.solana.actions.Action
import com.solana.api.Api
import com.solana.core.Account
import com.solana.models.Token
import com.solana.networking.NetworkingRouter
import com.solana.networking.socket.SolanaSocket
import com.solana.vendor.TokensListParser

interface SolanaAccountStorage {
    fun save(account: Account) : Result<Unit>
    fun account(): Result<Account>
    fun clear(): Result<Unit>
}

 class Solana(val router: NetworkingRouter){
    val api: Api = Api(router)
    val socket: SolanaSocket = SolanaSocket(router.endpoint)
    val supportedTokens: List<Token> by lazy {
        TokensListParser().parse(router.endpoint.network.name).getOrThrows()
    }
    val action: Action = Action(api, supportedTokens)
}
