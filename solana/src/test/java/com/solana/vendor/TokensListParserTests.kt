package com.solana.vendor

import org.junit.Test


class TokensListParserTests {

    @Test
    fun testParseSuccessfully() {
        val tokens = TokensListParser().parse("mainnet-beta").getOrThrows()
        assert(tokens.isNotEmpty())
    }
}