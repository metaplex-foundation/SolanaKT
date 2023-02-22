package com.solana.actions

import com.solana.api.Api
import com.solana.models.Token
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class Action(
    val api: Api,
    val supportedTokens: List<Token>,
    val dispatcher: CoroutineDispatcher = Dispatchers.IO
)
