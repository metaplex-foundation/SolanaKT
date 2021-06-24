package com.solana.models

import com.squareup.moshi.Json

class SplTokenAccountInfo(@Json(name = "value") val value: TokenResultObjects.Value) : RpcResultObject()