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
        // there is a weird bug in the testing framework (or possibly in suspendCoroutine) that
        // is causing the Result.exceptionOrNull() (and similar Result methods) to crash due to
        // a cast exceptions (Result<Result<T>> cant be cast to Result<T>). For some reason,
        // using Any as the type here and removing the getOrNull() call allows the test to pass.
        // This is weird behavior, that will likely be fixed when we upgrade our Kotlin version.
        // There is a similar bug reported here: https://youtrack.jetbrains.com/issue/KT-41163
        // TODO: Revert the commented code here
        // val result = solana.api.getConfirmedBlock(slot).getOrThrow()
        val result: Any = solana.api.getConfirmedBlock(slot)
        val tempFixConfirmBlock = result as ConfirmedBlock
        Assert.assertNotNull(tempFixConfirmBlock)
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

    @Test
    fun TestGetEpochInfo() = runTest {
        val result = solana.api.getEpochInfo().getOrThrow()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetEpochSchedule() = runTest {
        val result = solana.api.getEpochSchedule().getOrThrow()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetFeeCalculatorForBlockhash() = runTest {
        val solana = Solana(OkHttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val blockhash = solana.api.getRecentBlockhash().getOrThrow()
        val result = solana.api.getFeeCalculatorForBlockhash(blockhash).getOrThrow()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetFeeRateGovernor() = runTest {
        val result = solana.api.getFeeRateGovernor().getOrThrow()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetFees() = runTest {
        val result = solana.api.getFees().getOrThrow()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetFirstAvailableBlock() = runTest {
        val result = solana.api.getFirstAvailableBlock().getOrThrow()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetGenesisHash() = runTest {
        val result = solana.api.getGenesisHash().getOrThrow()
        Assert.assertNotNull(result)
    }
}