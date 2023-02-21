package com.solana

import com.solana.networking.HttpNetworkingRouter
import com.solana.networking.Network
import com.solana.networking.RPCEndpoint
import com.solana.networking.socket.SolanaSocket
import com.solana.solana.BuildConfig
import java.net.URL

object SolanaTestsUtils {
    const val RPC_URL = BuildConfig.RPC_URL
}

fun SolanaTestsUtils.generateSolanaConnection() =
    Solana(
        HttpNetworkingRouter(
            RPCEndpoint.custom(
                URL(RPC_URL),
                URL(RPC_URL),
                Network.devnet
            )
        )
    )

fun SolanaTestsUtils.generateSolanaSocket() = SolanaSocket(
    RPCEndpoint.custom(
        URL(RPC_URL),
        URL(RPC_URL),
        Network.devnet
    ),
    enableDebugLogs = true
)