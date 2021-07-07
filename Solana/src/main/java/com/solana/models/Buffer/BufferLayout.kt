package com.solana.models.Buffer

import com.solana.models.RpcSendTransactionConfig
import com.solana.vendor.borshj.Borsh
import com.solana.vendor.borshj.BorshCodable
import java.util.*

data class Buffer2<T>(val value: T?){
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