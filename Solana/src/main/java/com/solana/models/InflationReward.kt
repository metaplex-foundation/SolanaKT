package com.solana.models

import com.squareup.moshi.Json
import java.util.*

class InflationReward(pa: Map<String, Any>) {
    @Json(name = "epoch")
    private val epoch: Double = pa["epoch"] as Double

    @Json(name = "effectiveSlot")
    private val effectiveSlot: Double = pa["effectiveSlot"] as Double

    @Json(name = "amount")
    private val amount: Double = pa["amount"] as Double

    @Json(name = "postBalance")
    private val postBalance: Double = pa["postBalance"] as Double

}