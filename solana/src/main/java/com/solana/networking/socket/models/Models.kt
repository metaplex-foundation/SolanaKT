package com.solana.networking.socket.models

import kotlinx.serialization.Serializable

enum class SocketMethod(val string: String) {
    accountNotification("accountNotification"), accountSubscribe("accountSubscribe"), accountUnsubscribe("accountUnsubscribe"),
    signatureNotification("signatureNotification"), signatureSubscribe("signatureSubscribe"), signatureUnsubscribe("signatureUnsubscribe"),
    logsSubscribe("logsSubscribe"), logsNotification("logsNotification"), logsUnsubscribe("logsUnsubscribe"),
    programSubscribe("programSubscribe"), programNotification("programNotification"), programUnsubscribe("programUnsubscribe"),
    slotSubscribe("slotSubscribe"), slotNotification("slotNotification"), slotUnsubscribe("slotUnsubscribe")
}

@Serializable
data class SocketSubscription(
    val jsonrpc: String,
    val id: String,
    val result: Long
)

@Serializable
data class TokenAccountNotificationData (
    val program: String,
    val parsed: TokenAccountNotificationDataParsed
)

@Serializable
data class TokenAccountNotificationDataParsed (
    val type: String,
    val info: TokenAccountNotificationDataInfo
)

@Serializable
data class TokenAccountNotificationDataInfo (
    val tokenAmount: TokenAmount
)

@Serializable
data class TokenAmount (
    val address: String? = null,
    val amount: String,
    val decimals: Int,
    val uiAmount: Float
)

@Serializable
data class  SignatureNotification (
    val err: ResponseError?
)

@Serializable
data class ResponseError (
    val code: Int?,
    val message: String?,
    val data: ResponseErrorData?
)

@Serializable
data class  ResponseErrorData (
    val logs: List<String>
)

@Serializable
data class LogsNotification (
    val signature: String,
    val logs: List<String>,
    val err: ResponseError?
)
