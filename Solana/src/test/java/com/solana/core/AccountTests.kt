package com.solana.core

import com.solana.core.Account
import junit.framework.Assert.assertEquals
import org.bitcoinj.core.Base58
import org.junit.Assert
import org.junit.Test
import java.util.*
import java.util.logging.Logger


class AccountTests {
    @Test
    fun accountFromSecretKey() {
        val secretKey = Base58
            .decode("4Z7cXSyeFR8wNGMVXUE1TwtKn5D5Vu7FzEv69dokLv7KrQk7h6pu4LF8ZRR9yQBhc7uSM6RTTZtU1fmaxiNrxXrs")
        assertEquals(
            "QqCCvshxtqMAL2CVALqiJB7uEeE5mjSPsseQdDzsRUo",
            Account(secretKey).publicKey.toString()
        )
    }

    @Test
    fun fromBip44Mnemonic() {
        val acc = Account.fromMnemonic(
            Arrays.asList(
                "hint", "begin", "crowd", "dolphin", "drive", "render", "finger", "above", "sponsor", "prize", "runway", "invest", "dizzy", "pony", "bitter", "trial", "ignore", "crop", "please", "industry", "hockey", "wire", "use", "side"

            ), ""
            , DerivationPath.BIP44_M_44H_501H_0H
        )
        Assert.assertEquals("G75kGJiizyFNdnvvHxkrBrcwLomGJT2CigdXnsYzrFHv", acc.publicKey.toString())
    }

    @Test
    fun fromBip44MnemonicChange() {
        val acc = Account.fromMnemonic(
            Arrays.asList(
                "hint", "begin", "crowd", "dolphin", "drive", "render", "finger", "above", "sponsor", "prize", "runway", "invest", "dizzy", "pony", "bitter", "trial", "ignore", "crop", "please", "industry", "hockey", "wire", "use", "side"

            ), ""
            , DerivationPath.BIP44_M_44H_501H_0H_OH
        )
        Assert.assertEquals("AaXs7cLGcSVAsEt8QxstVrqhLhYN2iGhFNRemwYnHitV", acc.publicKey.toString())
    }

    @Test
    fun fromMnemonic() {
        val acc: Account = Account.fromMnemonic(
            Arrays.asList(
                "hint", "begin", "crowd", "dolphin", "drive", "render", "finger", "above", "sponsor", "prize", "runway", "invest", "dizzy", "pony", "bitter", "trial", "ignore", "crop", "please", "industry", "hockey", "wire", "use", "side"
            ), ""
        )
        assertEquals("AaXs7cLGcSVAsEt8QxstVrqhLhYN2iGhFNRemwYnHitV", acc.publicKey.toString())
    }

    @Test
    fun fromDepricatedMnemonicChange() {
        val acc = Account.fromMnemonic(
            Arrays.asList(
                "hint", "begin", "crowd", "dolphin", "drive", "render", "finger", "above", "sponsor", "prize", "runway", "invest", "dizzy", "pony", "bitter", "trial", "ignore", "crop", "please", "industry", "hockey", "wire", "use", "side"

            ), ""
            , DerivationPath.DEPRECATED_M_501H_0H_0_0
        )
        Assert.assertEquals("8knQfbiYmUfwsfcSihzX9FMU64GCc5XWcfcZtyNCoHSB", acc.publicKey.toString())
    }

    @Test
    fun fromJson() {
        val json =
            "[94,151,102,217,69,77,121,169,76,7,9,241,196,119,233,67,25,222,209,40,113,70,33,81,154,33,136,30,208,45,227,28,23,245,32,61,13,33,156,192,84,169,95,202,37,105,150,21,157,105,107,130,13,134,235,7,16,130,50,239,93,206,244,0]"
        val acc: Account = Account.fromJson(json)
        assertEquals("2cXAj2TagK3t6rb2CGRwyhF6sTFJgLyzyDGSWBcGd8Go", acc.publicKey.toString())
    }

}