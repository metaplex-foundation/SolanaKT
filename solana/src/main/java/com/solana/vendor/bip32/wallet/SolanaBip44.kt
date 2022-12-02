package com.solana.vendor.bip32.wallet

/**
 * Utility class for Solana BIP-44 paths
 */
class SolanaBip44 {
    private val hdKeyGenerator = HdKeyGenerator()
    private val solanaCoin: SolanaCoin
    private val PURPOSE: Long
    private val TYPE: Long
    private val ACCOUNT: Long
    private val CHANGE: Int

    /**
     * Get a root account address for a given seed using bip44 to match sollet implementation
     *
     * @param seed seed
     * @param derivableType bip44 derivableType
     * @return PrivateKey
     */
    fun getPrivateKeyFromSeed(seed: ByteArray, derivableType: DerivableType?, change: Int = CHANGE): ByteArray {
        return when (derivableType) {
            DerivableType.BIP44 -> getPrivateKeyFromBip44Seed(seed)
            DerivableType.BIP44CHANGE -> getPrivateKeyFromBip44SeedWithChange(seed, change)
            else -> throw RuntimeException("DerivableType not supported")
        }
    }

    private fun getPrivateKeyFromBip44SeedWithChange(seed: ByteArray, change: Int): ByteArray {
        val masterAddress = hdKeyGenerator.getAddressFromSeed(seed, solanaCoin)
        val purposeAddress =
            hdKeyGenerator.getAddress(masterAddress, PURPOSE, solanaCoin.alwaysHardened) // 44H
        val coinTypeAddress =
            hdKeyGenerator.getAddress(purposeAddress, TYPE, solanaCoin.alwaysHardened) // 501H
        val accountAddress =
            hdKeyGenerator.getAddress(coinTypeAddress, ACCOUNT, solanaCoin.alwaysHardened) //0H
        val changeAddress = hdKeyGenerator.getAddress(
            accountAddress,
            change.toLong(),
            solanaCoin.alwaysHardened
        )
        return changeAddress.privateKey.privateKey
    }

    private fun getPrivateKeyFromBip44Seed(seed: ByteArray): ByteArray {
        val masterAddress = hdKeyGenerator.getAddressFromSeed(seed, solanaCoin)
        val purposeAddress =
            hdKeyGenerator.getAddress(masterAddress, PURPOSE, solanaCoin.alwaysHardened) // 44H
        val coinTypeAddress =
            hdKeyGenerator.getAddress(purposeAddress, TYPE, solanaCoin.alwaysHardened) // 501H
        val accountAddress =
            hdKeyGenerator.getAddress(coinTypeAddress, ACCOUNT, solanaCoin.alwaysHardened) //0H
        return accountAddress.privateKey.privateKey
    }

    init {
        solanaCoin = SolanaCoin()
        PURPOSE = solanaCoin.purpose
        TYPE = solanaCoin.coinType
        ACCOUNT = 0
        CHANGE = 0
    }
}