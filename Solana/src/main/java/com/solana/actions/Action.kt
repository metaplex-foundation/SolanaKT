package com.solana.actions

import com.solana.api.Api
import com.solana.models.Token

class Action(val api: Api, val supportedTokens: List<Token>)
