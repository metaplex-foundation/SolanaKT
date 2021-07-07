package com.solana.socket

interface NotificationEventListener {
    fun onNotificationEvent(data: Any?)
}