package com.solana

import com.solana.networking.NetworkingRouter
import com.solana.networking.RPCEndpoint
import org.junit.Assert.assertTrue
import org.junit.Test


class SolanaTest {
    @Test
    fun someLibraryMethodReturnsTrue() {
        val classUnderTest = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        assertTrue(classUnderTest.someLibraryMethod())
    }
}
