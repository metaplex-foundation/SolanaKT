package com.solana.models.Buffer

import com.solana.models.RpcSendTransactionConfig
import com.solana.vendor.borshj.Borsh
import org.bitcoinj.core.Base58
import java.util.*

class Buffer<T: Borsh>{
    val value: T?
    constructor(rawData: Any, clazz: Class<T>) {
        if (rawData is String) {
            value = rawData as T
            return
        }

        val dataList = rawData as List<String>
        if(dataList[0].isBlank() || dataList[0].length <= 0){
            value = null
            return
        }

        val decodedBytes = decodedData(dataList[0],dataList[1])
        value = Borsh.deserialize(decodedBytes, clazz)
    }

    private fun decodedData(serializedData: String, encoding: String): ByteArray {
        return if (encoding == RpcSendTransactionConfig.Encoding.base64.toString()) {
            Base64.getDecoder().decode(serializedData)
        } else {
            Base58.decode(serializedData)
        }
    }
}
