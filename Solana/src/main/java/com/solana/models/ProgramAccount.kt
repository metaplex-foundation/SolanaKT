package com.solana.models

import com.squareup.moshi.Json
import org.bitcoinj.core.Base58
import java.util.*
import kotlin.collections.AbstractMap

class ProgramAccount(pa: Map<String, Any>) {
    class Account(acc: Any?) {
        @Json(name = "data") var data: String? = null

        @Json(name = "executable") val executable: Boolean

        @Json(name = "lamports") val lamports: Double

        @Json(name = "owner") val owner: String?

        @Json(name = "rentEpoch") val rentEpoch: Double

        private var encoding: String? = null
        val decodedData: ByteArray
            get() = if (encoding != null && encoding == RpcSendTransactionConfig.Encoding.base64.toString()) {
                Base64.getDecoder().decode(data)
            } else Base58.decode(data)

        init {
            val account = acc as Map<String, Any>
            val rawData = account["data"]
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

    @Json(name = "account") val account: Account = Account(pa["account"])
    @Json(name = "pubkey")  val pubkey: String = pa["pubkey"] as String
}