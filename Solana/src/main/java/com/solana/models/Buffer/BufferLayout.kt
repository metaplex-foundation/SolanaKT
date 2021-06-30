package com.solana.models.Buffer

import com.solana.models.RpcSendTransactionConfig
import org.bitcoinj.core.Base58
import java.util.*

data class LayoutEntry(val key: String?, val length: Int)
abstract class BufferLayout(open val layout: List<LayoutEntry>) {
    val BUFFER_LENGTH: Int get() {
        return this.layout.fold(0, { acc, next ->
            if (next.key != null) {
                acc + 0
            } else {
                acc + next.length
            }
        })
    }

    val span: Long get() { return this.layout.fold(0, { acc, next -> acc + next.length }).toLong() }
}

class Buffer<T>{
    val value: T?
    constructor(rawData: Any, layout: List<LayoutEntry>, clazz: Class<*>) {
        if (rawData is String) {
            value = rawData as T
            return
        }

        val dataList = rawData as List<String>
        val decodedData = decodedData(dataList[0],dataList[1])
        val bytes: ByteArray = decodedData
        val dict = mutableMapOf<String, ByteArray>()
        var from = 0
        layout.forEach {
            val to: Int = from + it.length
            it.key?.let {
                dict[it] = bytes.slice(IntRange(from, to - 1)).toByteArray()
            }
            from = to
        }
        value = clazz.constructors.first().newInstance(dict) as T
    }

    private fun decodedData(serializedData: String, encoding: String): ByteArray {
        return if (encoding == RpcSendTransactionConfig.Encoding.base64.toString()) {
            Base64.getDecoder().decode(serializedData)
        } else {
            Base58.decode(serializedData)
        }
    }
}
