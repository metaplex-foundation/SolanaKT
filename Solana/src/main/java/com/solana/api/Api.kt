package com.solana.api

import com.solana.models.*
import com.solana.networking.NetworkingRouter
import kotlin.collections.ArrayList


data class ApiError(override val message: String?) : Exception(message)

class Api(val router: NetworkingRouter)