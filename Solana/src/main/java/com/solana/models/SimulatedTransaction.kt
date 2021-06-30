package com.solana.models

import com.squareup.moshi.Json

class SimulatedTransaction(@Json(name = "value") val value: Value) : RpcResultObject() {
    class Value (
        @Json(name = "accounts") val accounts: List<SimulatedAccount.Value>,

        @Json(name = "logs")
        val logs: List<String>,
    )
}
class SimulatedAccount(
    @Json(name = "value") val value: Value
) : RpcResultObject() {
    class Value (
        @Json(name = "data")
        val data: List<String>,

        @Json(name = "executable")
        val executable:Boolean,

        @Json(name = "lamports")
        val lamports: Long,

        @Json(name = "owner")
        val owner: String?,

        @Json(name = "rentEpoch")
        val rentEpoch: Long,
    )
}
