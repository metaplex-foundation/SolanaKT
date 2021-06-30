package com.solana.models

import com.solana.models.Buffer.Buffer
import com.solana.models.Buffer.BufferLayout
import com.squareup.moshi.Json

class ProgramAccount<T>(pa: Map<String, Any>, clazz: Class<T>, bufferLayout: BufferLayout) {

    @Json(name = "account") val account: BufferInfo<T> = BufferInfo(pa["account"], clazz, bufferLayout)
    @Json(name = "pubkey")  val pubkey: String = pa["pubkey"] as String

    class BufferInfo<T>(acc: Any?, clazz: Class<T>, bufferLayout: BufferLayout){
        @Json(name = "data") var data: Buffer<T>? = null

        @Json(name = "executable") val executable: Boolean

        @Json(name = "lamports") val lamports: Double

        @Json(name = "owner") val owner: String?

        @Json(name = "rentEpoch") val rentEpoch: Double


        init {
            val account = acc as Map<String, Any>
            val rawData = account["data"]!!
            data = Buffer(rawData, bufferLayout, clazz)
            executable = account["executable"] as Boolean
            lamports = account["lamports"] as Double
            owner = account["owner"] as String?
            rentEpoch = account["rentEpoch"] as Double
        }
    }
}