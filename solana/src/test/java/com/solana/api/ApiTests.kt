@file:OptIn(ExperimentalCoroutinesApi::class)

package com.solana.api
import com.solana.Solana
import com.solana.core.HotAccount
import com.solana.core.PublicKey
import com.solana.models.ProgramAccountConfig
import com.solana.models.SignatureStatusRequestConfiguration
import com.solana.networking.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.serializer
import org.junit.Assert
import org.junit.Test
import java.lang.Error

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
        val result = solana.api.getConfirmedBlock(slot).getOrThrow()
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

    @Test
    fun TestGetIdentity() = runTest {
        val result = solana.api.getIdentity().getOrThrow()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetInflationRewardNullable() = runTest {
        val result = solana.api.getInflationReward(listOf(PublicKey("5U3bH5b6XtG99aVWLqwVzYPVpQiFHytBD68Rz2eFPZd7")))
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetMaxRetransmitSlot() = runTest {
        val result = solana.api.getMaxRetransmitSlot()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetMaxShredInsertSlot() = runTest {
        val result = solana.api.getMaxShredInsertSlot()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetMinimumBalanceForRentExemption() = runTest {
        val result = solana.api.getMinimumBalanceForRentExemption(32000)
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetSlot() = runTest {
        val result = solana.api.getSlot()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetSlotLeader() = runTest {
        val result = solana.api.getSlotLeader()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetSlotLeaders() = runTest {
        val slot = solana.api.getSlot().getOrThrow()
        val result = solana.api.getSlotLeaders(slot, 10)
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetSupply() = runTest {
        val result = solana.api.getSupply()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetTokenAccountBalance() = runTest {
        val result = solana.api.getTokenAccountBalance(PublicKey("FzhfekYF625gqAemjNZxjgTZGwfJpavMZpXCLFdypRFD"))
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetTokenSupply() = runTest {
        val result = solana.api.getTokenSupply(PublicKey("2tWC4JAdL4AxEFJySziYJfsAnW2MHKRo98vbAPiRDSk8"))
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetTransactionCount() = runTest {
        val result = solana.api.getTransactionCount()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetVersion() = runTest {
        val result = solana.api.getVersion()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetVoteAccounts() = runTest {
        val result = solana.api.getVoteAccounts()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestRequestAirdrop() = runTest {
        val result = solana.api.requestAirdrop(PublicKey("AaXs7cLGcSVAsEt8QxstVrqhLhYN2iGhFNRemwYnHitV"), 1010)
        Assert.assertNotNull(result)
    }

    @Test
    fun TestMinimumLedgerSlot() = runTest {
        val result = solana.api.minimumLedgerSlot()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetSignatureStatuses() = runTest {
        val result = solana.api.getSignatureStatuses(listOf("3citcRRbx1vTjXazYLXZ4cwVHNkx6baFrSNp5msR2mgTRuuod4qhqTi921emn2CjU93sSM5dGGhCcHeVtvQyPfCV"), SignatureStatusRequestConfiguration(true))
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetStakeActivation() = runTest {
        val result = solana.api.getStakeActivation(PublicKey("HDDhNo3H2t3XbLmRswHdTu5L8SvSMypz9UVFu68Wgmaf"))
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetSplTokenAccountInfo() = runTest {
        val mainnetSolana = Solana(OkHttpNetworkingRouter(RPCEndpoint.mainnetBetaSolana))
        val result = mainnetSolana.api.getSplTokenAccountInfo(PublicKey("D3PSQUMEYyDWvNxaPrAhv2ZxMcrCMRqTUD5LHm4HLrAR"))
        Assert.assertNotNull(result)
    }

    //region getProgramAccounts
    @Test
    fun testGetProgramAccountsReturnsValidAccountInfo() = runTest {
        // given
        val account = "accountAddress"
        val request = ProgramAccountRequest(account)
        val expectedAccounts = listOf(AccountInfoWithPublicKey(
            AccountInfo("programAccount", false, 0, "", 0),
            account
        ))

        val solanaDriver = Api(MockRpcDriver().apply {
            willReturn(request, expectedAccounts)
        })

        // when
        val actualAccounts = solanaDriver
            .getProgramAccounts(String.serializer(), PublicKey(account), ProgramAccountConfig())
            .getOrDefault(listOf())

        // then
        Assert.assertEquals(expectedAccounts, actualAccounts)
    }

    @Test
    fun testGetProgramAccountsReturnsEmptyListForUnknownAccount() = runTest {
        // given
        val account = "accountAddress"
        val expectedAccounts = listOf<AccountInfoWithPublicKey<String>>()
        val solanaDriver = Api(MockRpcDriver())

        // when
        val actualAccounts = solanaDriver
            .getProgramAccounts(String.serializer(), PublicKey(account), ProgramAccountConfig())
            .getOrNull()

        // then
        Assert.assertEquals(expectedAccounts, actualAccounts)
    }

    @Test
    fun testGetProgramAccountsReturnsErrorForInvalidParams() = runTest {
        // given
        val account = "accountAddress"
        val expectedErrorMessage = "Error Message"
        val expectedResult = Result.failure<String>(Error(expectedErrorMessage))
        val solanaDriver = Api(MockRpcDriver().apply {
            willError(ProgramAccountRequest(account), RpcError(1234, expectedErrorMessage))
        })

        // when
        val actualResult = solanaDriver.getProgramAccounts(
            String.serializer(), PublicKey(account), ProgramAccountConfig()
        )

        // then
        Assert.assertEquals(expectedResult.isFailure, actualResult.isFailure)
        Assert.assertEquals(expectedErrorMessage, actualResult.exceptionOrNull()?.message)
    }
    //endregion

    //region getMultipleAccountsInfo
    @Test
    fun testGetMultipleAccountsInfoReturnsValidAccountInfo() = runTest {
        // given
        val accounts = listOf(HotAccount().publicKey)
        val accountsRequest = MultipleAccountsRequest(accounts.map { it.toBase58() })
        val expectedAccountInfo = listOf(AccountInfo("testAccount", false, 0, "", 0))
        val solanaDriver = Api(MockRpcDriver().apply {
            willReturn(accountsRequest, expectedAccountInfo)
        })

        // when
        val actualAccountInfo = solanaDriver.getMultipleAccountsInfo<String>(serializer(), accounts).getOrNull()

        // then
        Assert.assertEquals(expectedAccountInfo, actualAccountInfo)
    }

    @Test
    fun testGetMultipleAccountsInfoReturnsEmptyListForNullAccount() = runTest {
        // given
        val accounts = listOf(HotAccount().publicKey)
        val expectedAccountInfo = listOf<String>()
        val solanaDriver = Api(MockRpcDriver())

        // when
        val actualAccountInfo = solanaDriver.getMultipleAccountsInfo<String>(serializer(), accounts).getOrNull()

        // then
        Assert.assertEquals(expectedAccountInfo, actualAccountInfo)
    }
    //endregion
}
