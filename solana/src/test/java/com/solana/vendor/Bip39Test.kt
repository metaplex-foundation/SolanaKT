package com.solana.vendor

import com.solana.core.HotAccount
import com.solana.vendor.bip39.Mnemonic
import com.solana.vendor.bip39.WordCount
import org.junit.Assert
import org.junit.Test

class Bip39Test {
    private val validPhrase = listOf("hint", "begin", "crowd", "dolphin", "drive", "render", "finger", "above", "sponsor", "prize", "runway", "invest", "dizzy", "pony", "bitter", "trial", "ignore", "crop", "please", "industry", "hockey", "wire", "use", "side")

    @Test
    fun test24WordMnemonic() {
        val phrase = Mnemonic(WordCount.COUNT_24).phrase
        Assert.assertEquals(phrase.size, 24)
    }

    @Test
    fun test12WordMnemonic() {
        val phrase = Mnemonic(WordCount.COUNT_12).phrase
        Assert.assertEquals(phrase.size, 12)
    }

    @Test
    fun test21WordMnemonic() {
        val phrase = Mnemonic(WordCount.COUNT_21).phrase
        Assert.assertEquals(phrase.size, 21)
    }

    @Test
    fun testValidateGeneratedMnemonic() {
        val phrase = Mnemonic(WordCount.COUNT_24).phrase
        Mnemonic(phrase = phrase).validate()
    }

    @Test
    fun testValidateExternalMnemonic() {
        Mnemonic(phrase = validPhrase).validate()
    }

    @Test
    fun testValidateChecksumGeneratedMnemonic() {
        val phrase = Mnemonic(WordCount.COUNT_24).phrase
        Mnemonic(phrase = phrase).validateChecksum()
    }

    @Test
    fun testValidateChecksumExternalMnemonic() {
        Mnemonic(phrase = validPhrase).validateChecksum()
    }

    @Test
    fun testGenerate24AccountWithPhrase() {
        val mnemonic = Mnemonic(WordCount.COUNT_24)
        val account = HotAccount.fromMnemonic(mnemonic.phrase, mnemonic.passphrase)
        Assert.assertNotNull(account)
    }

    @Test
    fun testGenerate12AccountWithPhrase() {
        val mnemonic = Mnemonic(WordCount.COUNT_12)
        val account = HotAccount.fromMnemonic(mnemonic.phrase, mnemonic.passphrase)
        Assert.assertNotNull(account)
    }

    @Test
    fun testGenerate21AccountWithPhrase() {
        val mnemonic = Mnemonic(WordCount.COUNT_21)
        val account = HotAccount.fromMnemonic(mnemonic.phrase, mnemonic.passphrase)
        Assert.assertNotNull(account)
    }

    @Test
    fun testGenerate15AccountWithPhrase() {
        val mnemonic = Mnemonic(WordCount.COUNT_15)
        val account = HotAccount.fromMnemonic(mnemonic.phrase, mnemonic.passphrase)
        Assert.assertNotNull(account)
    }
}