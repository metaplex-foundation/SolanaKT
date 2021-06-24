package com.solana.models

import com.squareup.moshi.Json

class VoteAccountConfig (
    @Json(name = "votePubkey") val votePubkey: String? = null
)