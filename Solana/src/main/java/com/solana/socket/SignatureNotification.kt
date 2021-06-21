package com.solana.socket

class SignatureNotification(val error: Any?) {

    fun hasError(): Boolean {
        return error != null
    }
}