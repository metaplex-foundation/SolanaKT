package com.solana.models

import com.solana.core.PublicKeyRule
import com.solana.models.Buffer.*
import com.solana.vendor.borshj.Borsh
import com.solana.vendor.borshj.BorshCodable
import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson
import org.bitcoinj.core.Base58
import java.util.*

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
        borsh.setRules(listOf(PublicKeyRule(), AccountInfoRule(), MintRule(), TokenSwapInfoRule()))
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


@JsonClass(generateAdapter = true)
data class RPC2<T: BorshCodable>(
    var context: Context?,
    val value: BufferInfo2<T>?
) {
    @JsonClass(generateAdapter = true)
    class Context (val slot: Long)
}

@JsonClass(generateAdapter = true)
data class BufferInfo2<T: BorshCodable>(
    var data: Buffer2<T>? = null,
    val executable: Boolean,
    val lamports: Double,
    val owner: String?,
    val rentEpoch: Double
)

class AccountInfoJsonAdapter(val borsh: Borsh) {
    @FromJson
    fun fromJson(rawData: Any): Buffer2<AccountInfo> {
        return Buffer2.create(borsh, rawData, AccountInfo::class.java)
    }
}

data class Buffer2<T: BorshCodable>(val value: T?){
    companion object {
        fun <T: BorshCodable>create(borsh: Borsh, rawData: Any, clazz: Class<T>): Buffer2<T> {
            if (rawData is String) {
                return Buffer2(clazz.cast(rawData))
            }

            val dataList = rawData as List<String>
            if(dataList[0].isBlank() || dataList[0].length <= 0){
                return Buffer2(null)
            }

            val serializedData = dataList[0]
            val encoding = dataList[1]

            return if(encoding == RpcSendTransactionConfig.Encoding.base64.toString()) {
                val decodedBytes = Base64.getDecoder().decode(serializedData)
                Buffer2(borsh.deserialize(decodedBytes, clazz))
            } else if(encoding == RpcSendTransactionConfig.Encoding.base58.toString()){
                //Base58.decode(serializedData)
                return Buffer2(null)
            } else {
                return Buffer2(null)
            }
        }
    }

}