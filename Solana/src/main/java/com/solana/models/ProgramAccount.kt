package com.solana.models

import com.squareup.moshi.Json
import org.bitcoinj.core.Base58
import java.util.*

class ProgramAccount {
    inner class Account(acc: Any) {
        @Json(name = "data")
        var data: String? = null

        @Json(name = "executable")
        val isExecutable: Boolean

        @Json(name = "lamports")
        val lamports: Double

        @Json(name = "owner")
        val owner: String?

        @Json(name = "rentEpoch")
        val rentEpoch: Double
        private var encoding: String? = null
        val decodedData: ByteArray
            get() = if (encoding == RpcSendTransactionConfig.Encoding.base64.toString()) {
                Base64.getDecoder().decode(data)
            } else Base58.decode(data)

        init {
            val account = acc as AbstractMap<*, *>
            val rawData = account["data"]
            if (rawData is List<*>) {
                data = rawData[0] as String
                encoding = rawData[1] as String
            } else if (rawData is String) {
                data = rawData
            }
            isExecutable = account["executable"] as Boolean
            lamports = account["lamports"] as Double
            owner = account["owner"] as String?
            rentEpoch = account["rentEpoch"] as Double
        }
    }

    @Json(name = "account")
    var account: Account? = null
        private set

    @Json(name = "pubkey")
    var pubkey: String? = null
        private set

    constructor() {}
    constructor(pa: Map<String, Any>) {
        account = Account(pa["account"]!!)
        pubkey = pa["pubkey"] as String?
    }
}