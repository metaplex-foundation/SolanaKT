package com.solana.api

import com.solana.networking.NetworkingRouter


data class ApiError(override val message: String?) : Exception(message)

class Api(val router: NetworkingRouter)