package com.solana.networking.socket.models

import com.solana.core.PublicKey
import com.squareup.moshi.JsonClass

enum class SocketMethod(val string: String) {
    accountNotification("accountNotification"), accountSubscribe("accountSubscribe"), accountUnsubscribe("accountUnsubscribe"),
    signatureNotification("signatureNotification"), signatureSubscribe("signatureSubscribe"), signatureUnsubscribe("signatureUnsubscribe"),
    logsSubscribe("logsSubscribe"), logsNotification("logsNotification"), logsUnsubscribe("logsUnsubscribe"),
    programSubscribe("programSubscribe"), programNotification("programNotification"), programUnsubscribe("programUnsubscribe"),
    slotSubscribe("slotSubscribe"), slotNotification("slotNotification"), slotUnsubscribe("slotUnsubscribe")
}

@JsonClass(generateAdapter = true)
data class SocketSubscription(
    val jsonrpc: String,
    val id: String,
    val result: Long
)

@JsonClass(generateAdapter = true)
data class TokenAccountNotificationData (
    val program: String,
    val parsed: TokenAccountNotificationDataParsed
)

@JsonClass(generateAdapter = true)
data class TokenAccountNotificationDataParsed (
    val type: String,
    val info: TokenAccountNotificationDataInfo
)

@JsonClass(generateAdapter = true)
data class TokenAccountNotificationDataInfo (
    val tokenAmount: TokenAmount
)

@JsonClass(generateAdapter = true)
data class TokenAmount (
    val address: String?,
    val amount: String,
    val decimals: Int,
    val uiAmount: Float
)

@JsonClass(generateAdapter = true)
data class  SignatureNotification (
    val err: ResponseError?
)

@JsonClass(generateAdapter = true)
data class ResponseError (
    val code: Int?,
    val message: String?,
    val data: ResponseErrorData?
)

@JsonClass(generateAdapter = true)
data class  ResponseErrorData (
    val logs: List<String>
)

@JsonClass(generateAdapter = true)
data class LogsNotification (
    val signature: String,
    val logs: List<String>,
    val err: ResponseError?
)
