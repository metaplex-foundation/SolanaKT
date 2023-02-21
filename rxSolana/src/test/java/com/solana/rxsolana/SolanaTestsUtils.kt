package com.solana.rxsolana

import com.solana.Solana
import com.solana.networking.HttpNetworkingRouter
import com.solana.networking.Network
import com.solana.networking.RPCEndpoint
import com.solana.solana.BuildConfig
import java.net.URL

object SolanatestsUtils {
    const val RPC_URL = BuildConfig.RPC_URL
}

fun SolanatestsUtils.generateSolanaConnection() =
    Solana(
        HttpNetworkingRouter(
            RPCEndpoint.custom(
                URL(RPC_URL),
                URL(RPC_URL),
                Network.devnet
            )
        )
    )
