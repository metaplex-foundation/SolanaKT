package com.solana.models


class RpcSendTransactionConfig(
    val encoding: String = Encoding.base64.getEncoding(),
){
    enum class Encoding(private val enc: String) {
        base64("base64"),
        base58("base58"),
        jsonParsed("jsonParsed");
        fun getEncoding(): String {
            return enc
        }
    }
}