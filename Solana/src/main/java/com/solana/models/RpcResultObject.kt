package com.solana.models

import com.solana.models.Buffer.Buffer
import com.solana.vendor.borshj.Borsh
import com.solana.vendor.borshj.BorshCodable
import com.squareup.moshi.Json

open class RpcResultObject(@Json(name = "context") var context: Context? = null) {
    class Context (
        @Json(name = "slot") val slot: Long
    )
}

class BufferInfo<T: BorshCodable>(acc: Any?, clazz: Class<T>){
    @Json(name = "data") var data: Buffer<T>? = null
    @Json(name = "executable") val executable: Boolean
    @Json(name = "lamports") val lamports: Double
    @Json(name = "owner") val owner: String?
    @Json(name = "rentEpoch") val rentEpoch: Double


    init {
        val borsh = Borsh()
        val account = acc as Map<String, Any>
        val rawData = account["data"]!!
        data = Buffer(borsh, rawData, clazz)
        executable = account["executable"] as Boolean
        lamports = account["lamports"] as Double
        owner = account["owner"] as String?
        rentEpoch = account["rentEpoch"] as Double
    }
}

class RPC<T: BorshCodable>(pa: Map<String, Any>, clazz: Class<T>){

    @Json(name = "context") var context: Context?
    @Json(name = "value") val value: BufferInfo<T>?

    init {
        context = Context(pa["context"] as Map<String, Any>)
        value = BufferInfo(pa["value"], clazz)
    }
    class Context (pa: Map<String, Any>){
        init {
            @Json(name = "slot") val slot: Long = (pa["slot"] as Double).toLong()
        }
    }
}
