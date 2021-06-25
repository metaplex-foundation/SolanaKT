package com.solana

import com.solana.actions.Action
import com.solana.api.Api
import com.solana.networking.NetworkingRouter

class Solana(val router: NetworkingRouter){
    val api: Api = Api(router)
    val action: Action = Action(api)
}
