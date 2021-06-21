package com.solana.networking

import java.net.URL

const val MAINNETBETA = "mainnet-beta"
const val DEVNET = "devnet"
const val TESTNET = "testnet"

public sealed class Network(val name: String) {
    object mainnetBeta: Network(MAINNETBETA)
    object devnet: Network(DEVNET)
    object testnet: Network(TESTNET)
    var cluster: String = this.name
}


public sealed class RPCEndpoint(open val url: URL, open val network: Network) {
    object mainnetBetaSerum: RPCEndpoint(URL("https://solana-api.projectserum.com"), Network.mainnetBeta)
    object mainnetBetaSolana: RPCEndpoint(URL("https://api.mainnet-beta.solana.com"), Network.mainnetBeta)
    object devnetSolana: RPCEndpoint(URL("https://api.devnet.solana.com"), Network.devnet)
    object testnetSolana: RPCEndpoint(URL("https://testnet.solana.com"), Network.testnet)
    data class custom(override val url: URL, override val network: Network) : RPCEndpoint(url, network)
}