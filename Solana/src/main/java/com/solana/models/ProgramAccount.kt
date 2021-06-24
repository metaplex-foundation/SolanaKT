package com.solana.models

import com.squareup.moshi.Json
import org.bitcoinj.core.Base58
import java.util.*
import kotlin.collections.AbstractMap

class ProgramAccount(pa: AbstractMap<*, *>) {
    class Account(acc: Any?) {
        @Json(name = "data")
        private var data: String? = null

        @Json(name = "executable")
        private val executable: Boolean

        @Json(name = "lamports")
        private val lamports: Double

        @Json(name = "owner")
        private val owner: String?

        @Json(name = "rentEpoch")
        private val rentEpoch: Double
        private var encoding: String? = null
        val decodedData: ByteArray
            get() = if (encoding != null && encoding == Encoding.base64.toString()) {
                Base64.getDecoder().decode(data)
            } else Base58.decode(data)

        init {
            val account = acc as AbstractMap<*, *>?
            val rawData = account!!["data"]
            if (rawData is List<*>) {
                val dataList = rawData as List<String>
                data = dataList[0]
                encoding = dataList[1]
            } else if (rawData is String) {
                data = rawData
            }
            executable = account["executable"] as Boolean
            lamports = account["lamports"] as Double
            owner = account["owner"] as String?
            rentEpoch = account["rentEpoch"] as Double
        }
    }

    @Json(name = "account")
    private val account: Account

    @Json(name = "pubkey")
    private val pubkey: String?

    init {
        account = Account(pa["account"])
        pubkey = pa["pubkey"] as String?
    }
}