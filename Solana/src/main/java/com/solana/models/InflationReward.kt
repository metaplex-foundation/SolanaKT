package com.solana.models

import com.squareup.moshi.Json
import java.util.*

class InflationReward(pa: AbstractMap<*, *>) {
    @Json(name = "epoch")
    private val epoch: Double

    @Json(name = "effectiveSlot")
    private val effectiveSlot: Double

    @Json(name = "amount")
    private val amount: Double

    @Json(name = "postBalance")
    private val postBalance: Double

    // Constructor for deserializing into List
    init {
        epoch = (pa["epoch"] as Double?)!!
        effectiveSlot = (pa["effectiveSlot"] as Double?)!!
        amount = (pa["amount"] as Double?)!!
        postBalance = (pa["postBalance"] as Double?)!!
    }
}