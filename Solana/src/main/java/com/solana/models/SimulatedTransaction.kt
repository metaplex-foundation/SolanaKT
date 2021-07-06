package com.solana.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SimulatedTransaction(
    override val value: Value
) : RpcResultObject<SimulatedTransaction.Value>(null, value) {

    @JsonClass(generateAdapter = true)
    class Value (
        val accounts: List<SimulatedAccount.Value>,
        val logs: List<String>,
    )
}

@JsonClass(generateAdapter = true)
class SimulatedAccount(
    override val value: Value
) : RpcResultObject<SimulatedAccount.Value>(null, value) {

    @JsonClass(generateAdapter = true)
    class Value (
        val data: List<String>,
        val executable:Boolean,
        val lamports: Long,
        val owner: String?,
        val rentEpoch: Long,
    )
}
