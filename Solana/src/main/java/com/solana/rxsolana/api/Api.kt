package com.solana.rxsolana.api

import com.solana.models.*
import com.solana.networking.NetworkingRouter

data class ApiError(override val message: String?) : Exception(message)

class Api(private val router: NetworkingRouter) {
    fun getRecentBlockhash(onComplete: ((Result<RecentBlockhash>) -> Unit)) {
        return router.call("getRecentBlockhash", null, RecentBlockhash::class.java, onComplete)
    }
}

