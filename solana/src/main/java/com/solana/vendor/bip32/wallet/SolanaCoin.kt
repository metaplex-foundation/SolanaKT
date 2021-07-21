package com.solana.vendor.bip32.wallet

import com.solana.vendor.bip32.wallet.key.SolanaCurve

class SolanaCoin {
    /**
     * Get the curve
     *
     * @return curve
     */
    val curve = SolanaCurve()

    /**
     * get the coin type
     *
     * @return coin type
     */
    val coinType: Long = 501

    /**
     * get the coin purpose
     *
     * @return purpose
     */
    val purpose: Long = 44

    /**
     * get whether the addresses must always be hardened
     *
     * @return always hardened
     */
    val alwaysHardened = true
}