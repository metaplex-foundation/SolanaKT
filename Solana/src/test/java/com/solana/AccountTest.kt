package com.solana

import com.solana.models.Account
import junit.framework.TestCase.assertEquals
import org.bitcoinj.core.Base58
import org.junit.Test
import java.util.*

class AccountTest {
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
}
