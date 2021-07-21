package com.solana.models.buffer

import com.solana.models.RpcSendTransactionConfig
import com.solana.vendor.borshj.Borsh
import com.solana.vendor.borshj.BorshCodable
import java.util.*

data class Buffer<T>(val value: T?){
    companion object {
        fun <T: BorshCodable>create(borsh: Borsh, rawData: Any, clazz: Class<T>): Buffer<T> {
            if (rawData is String) {
                return Buffer(clazz.cast(rawData))
            }

            val dataList = rawData as List<String>
            if(dataList[0].isBlank() || dataList[0].length <= 0){
                return Buffer(null)
            }

            val serializedData = dataList[0]
            val encoding = dataList[1]

            return when (encoding) {
                RpcSendTransactionConfig.Encoding.base64.toString() -> {
                    val decodedBytes = Base64.getDecoder().decode(serializedData)
                    Buffer(borsh.deserialize(decodedBytes, clazz))
                }
                RpcSendTransactionConfig.Encoding.base58.toString() -> {
                    //Base58.decode(serializedData)
                    return Buffer(null)
                }
                else -> {
                    return Buffer(null)
                }
            }
        }
    }
}