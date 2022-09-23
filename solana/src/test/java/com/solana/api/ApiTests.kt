@file:OptIn(ExperimentalCoroutinesApi::class)

package com.solana.api
import com.solana.Solana
import com.solana.core.PublicKey
import com.solana.networking.OkHttpNetworkingRouter
import com.solana.networking.RPCEndpoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class ApiTests {

    val solana: Solana get() = Solana(OkHttpNetworkingRouter(RPCEndpoint.devnetSolana))

    @Test
    fun TestGetRecentBlockhash() = runTest {
        val result = solana.api.getRecentBlockhash().getOrThrow()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetBlock() = runTest {
        val result = solana.api.getBlock(164039401).getOrThrow()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetBlockCommitment() = runTest {
        val result = solana.api.getBlockCommitment(82493733).getOrThrow()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetBlockHeight() = runTest {
        val result = solana.api.getBlockHeight().getOrThrow()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetBlockTime() = runTest {
        val height = solana.api.getBlockHeight().getOrThrow()
        val result = solana.api.getBlockTime(height)
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetClusterNodes() = runTest {
        val result = solana.api.getClusterNodes().getOrThrow()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetConfirmedBlock() = runTest {
        val slot = solana.api.getSnapshotSlot().getOrThrow()
        val result: Any = solana.api.getConfirmedBlock(slot)
        Assert.assertNotNull(result)
    }

    @Test
    fun TestSnapshotSlotBlock() = runTest {
        val result = solana.api.getSnapshotSlot().getOrThrow()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetConfirmedBlocks() = runTest {
        val height = solana.api.getBlockHeight().getOrThrow()
        val result = solana.api.getConfirmedBlocks(height, height - 10).getOrThrow()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetConfirmedSignaturesForAddress2() = runTest {
        val result = solana.api.getConfirmedSignaturesForAddress2(PublicKey("5Zzguz4NsSRFxGkHfM4FmsFpGZiCDtY72zH2jzMcqkJx"), 10).getOrThrow()
        Assert.assertNotNull(result)
    }
}