package com.solana

import com.solana.core.Account
import junit.framework.Assert.assertEquals
import org.bitcoinj.core.Base58
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
    fun fromMnemonic() {
        val acc: Account = Account.fromMnemonic(
            Arrays.asList(
                "spider", "federal", "bleak", "unable", "ask", "weasel",
                "diamond", "electric", "illness", "wheat", "uphold", "mind"
            ), ""
        )
        assertEquals("BQWWFhzBdw2vKKBUX17NHeFbCoFQHfRARpdztPE2tDJ", acc.publicKey.toString())
    }

    @Test
    fun fromJson() {
        val json =
            "[94,151,102,217,69,77,121,169,76,7,9,241,196,119,233,67,25,222,209,40,113,70,33,81,154,33,136,30,208,45,227,28,23,245,32,61,13,33,156,192,84,169,95,202,37,105,150,21,157,105,107,130,13,134,235,7,16,130,50,239,93,206,244,0]"
        val acc: Account = Account.fromJson(json)
        assertEquals("2cXAj2TagK3t6rb2CGRwyhF6sTFJgLyzyDGSWBcGd8Go", acc.publicKey.toString())
    }

    @Test
    fun fromBip39MnemonicTest() {
        val account: Account = Account.fromBip39Mnemonic(
            Arrays.asList(
                "iron", "make", "indoor", "where", "explain", "model", "maximum", "wonder",
                "toward", "salad", "fan", "try"
            ),
            ""
        )
        Logger.getAnonymousLogger().info("Derived pubkey = " + account.publicKey.toBase58())
        assertEquals(
            "BeepMww3KwiDeEhEeZmqk4TegvJYNuDERPWm142X6Mx3",
            account.publicKey.toBase58()
        )
    }
}