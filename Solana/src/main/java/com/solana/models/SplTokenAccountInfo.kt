package com.solana.models

import com.squareup.moshi.Json

const val MINIMUM_BALANCE_FOR_RENT_EXEMPTION_165 = 2039280L
const val REQUIRED_ACCOUNT_SPACE = 165L
class SplTokenAccountInfo(@Json(name = "value") val value: TokenResultObjects.Value) : RpcResultObject()