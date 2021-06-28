package com.solana.vendor.bip32.wallet

import com.solana.vendor.bip32.wallet.key.HdPrivateKey
import com.solana.vendor.bip32.wallet.key.HdPublicKey

/**
 * An HD pub/private key
 */
class HdAddress(
    val privateKey: HdPrivateKey,
    val publicKey: HdPublicKey,
    val coinType: SolanaCoin,
    val path: String
)