package com.solana.networking.socket.models

import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

enum class SocketMethod(val string: String) {
    accountNotification("accountNotification"), accountSubscribe("accountSubscribe"), accountUnsubscribe("accountUnsubscribe"),
    signatureNotification("signatureNotification"), signatureSubscribe("signatureSubscribe"), signatureUnsubscribe("signatureUnsubscribe"),
    logsSubscribe("logsSubscribe"), logsNotification("logsNotification"), logsUnsubscribe("logsUnsubscribe"),
    programSubscribe("programSubscribe"), programNotification("programNotification"), programUnsubscribe("programUnsubscribe"),
    slotSubscribe("slotSubscribe"), slotNotification("slotNotification"), slotUnsubscribe("slotUnsubscribe")
}

@Serializable
@JsonClass(generateAdapter = true)
data class SocketSubscription(
    val jsonrpc: String,
    val id: String,
    val result: Long
)

@Serializable
@JsonClass(generateAdapter = true)
data class TokenAccountNotificationData (
    val program: String,
    val parsed: TokenAccountNotificationDataParsed
)

@Serializable
@JsonClass(generateAdapter = true)
data class TokenAccountNotificationDataParsed (
    val type: String,
    val info: TokenAccountNotificationDataInfo
)

@Serializable
@JsonClass(generateAdapter = true)
data class TokenAccountNotificationDataInfo (
    val tokenAmount: TokenAmount
)

@Serializable
@JsonClass(generateAdapter = true)
data class TokenAmount (
    val address: String? = null,
    val amount: String,
    val decimals: Int,
    val uiAmount: Float
)

@Serializable
@JsonClass(generateAdapter = true)
data class  SignatureNotification (
    val err: ResponseError?
)

@Serializable
@JsonClass(generateAdapter = true)
data class ResponseError (
    val code: Int?,
    val message: String?,
    val data: ResponseErrorData?
)

@Serializable
@JsonClass(generateAdapter = true)
data class  ResponseErrorData (
    val logs: List<String>
)

@Serializable
@JsonClass(generateAdapter = true)
data class LogsNotification (
    val signature: String,
    val logs: List<String>,
    val err: ResponseError?
)
