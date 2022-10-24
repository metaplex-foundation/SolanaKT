package com.solana.rxsolana.api

import com.solana.Solana
import com.solana.api.SolanaAccountSerializer
import com.solana.core.PublicKey
import com.solana.models.SignatureStatusRequestConfiguration
import com.solana.models.buffer.AccountInfoData
import com.solana.networking.HttpNetworkingRouter
import com.solana.networking.RPCEndpoint
import org.junit.Assert
import org.junit.Test
import kotlin.collections.listOf

class Methods {
    @Test
    fun TestGetRecentBlockhash() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getRecentBlockhash().blockingGet()
        Assert.assertNotNull(result)
    }
    @Test
    fun TestGetBalance() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getBalance(PublicKey("AaXs7cLGcSVAsEt8QxstVrqhLhYN2iGhFNRemwYnHitV")).blockingGet()
        Assert.assertTrue(result > 0)
    }

    /*@Test
    fun TestGetConfirmedTransaction() {
        val solana = Solana(OkHttpNetworkingRouter(RPCEndpoint.devnetSolana))

        val slot = solana.api.getSnapshotSlot().blockingGet()
        val block = solana.api.getConfirmedBlock(slot.toInt()).blockingGet()
        val signature = block.transactions!!.first().transaction!!.signatures.first()
        val result = solana.api.getConfirmedTransaction(signature).blockingGet()
        Assert.assertTrue(result.slot!! > 0)
        Assert.assertEquals(result.transaction!!.signatures[0], signature)
    }*/

    @Test
    fun TestGetVoteAccounts() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getVoteAccounts().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetStakeActivation() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getStakeActivation(PublicKey("HDDhNo3H2t3XbLmRswHdTu5L8SvSMypz9UVFu68Wgmaf")).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetStakeActivationEpoch() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getStakeActivation(PublicKey("HDDhNo3H2t3XbLmRswHdTu5L8SvSMypz9UVFu68Wgmaf"), 143).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestRequestAirdrop() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.requestAirdrop(PublicKey("AaXs7cLGcSVAsEt8QxstVrqhLhYN2iGhFNRemwYnHitV"), 1010).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetMinimumBalanceForRentExemption() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getMinimumBalanceForRentExemption(32000).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetBlockTime() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.mainnetBetaSolana))
        val height = solana.api.getBlockHeight().blockingGet()
        val result = solana.api.getBlockTime(height-1).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetAccountInfo() {
        val solanaDevNet = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solanaDevNet.api.getAccountInfo(SolanaAccountSerializer(AccountInfoData.serializer()), PublicKey("AaXs7cLGcSVAsEt8QxstVrqhLhYN2iGhFNRemwYnHitV")).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetBlockHeight() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getBlockHeight().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetVersion() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getVersion().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestMinimumLedgerSlot() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.minimumLedgerSlot().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetFeeCalculatorForBlockhash() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val blockhash = solana.api.getRecentBlockhash().blockingGet()
        val result = solana.api.getFeeCalculatorForBlockhash(blockhash).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetBlockCommitment() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getBlockCommitment(82493733).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetFeeRateGovernor() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getFeeRateGovernor().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetFees() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getFees().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetTransactionCount() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getTransactionCount().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetMaxRetransmitSlot() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getMaxRetransmitSlot().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetSupply() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.mainnetBetaSolana))
        val result = solana.api.getSupply().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetFirstAvailableBlock() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getFirstAvailableBlock().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetGenesisHash() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getGenesisHash().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetEpochInfo() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getEpochInfo().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetEpochSchedule() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getEpochSchedule().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetConfirmedBlock() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val slot = solana.api.getSnapshotSlot().blockingGet()
        val result = solana.api.getConfirmedBlock(slot.toInt()).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetSnapshotSlot() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getSnapshotSlot().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetMaxShredInsertSlot() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getMaxShredInsertSlot().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetSlot() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getSlot().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetConfirmedBlocks() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val height = solana.api.getBlockHeight().blockingGet().toInt()
        val result = solana.api.getConfirmedBlocks(height, height - 10).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetSplTokenAccountInfo() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.mainnetBetaSolana))
        val result = solana.api.getSplTokenAccountInfo(PublicKey("D3PSQUMEYyDWvNxaPrAhv2ZxMcrCMRqTUD5LHm4HLrAR")).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetSlotLeader() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getSlotLeader().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetClusterNodes() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getClusterNodes().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetTokenAccountBalance() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getTokenAccountBalance(PublicKey("FzhfekYF625gqAemjNZxjgTZGwfJpavMZpXCLFdypRFD")).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetConfirmedSignaturesForAddress2() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getConfirmedSignaturesForAddress2(PublicKey("5Zzguz4NsSRFxGkHfM4FmsFpGZiCDtY72zH2jzMcqkJx"), 10, null, null).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetSignatureStatuses() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getSignatureStatuses(listOf("3citcRRbx1vTjXazYLXZ4cwVHNkx6baFrSNp5msR2mgTRuuod4qhqTi921emn2CjU93sSM5dGGhCcHeVtvQyPfCV"), SignatureStatusRequestConfiguration(true)).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetTokenSupply() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getTokenSupply(PublicKey("2tWC4JAdL4AxEFJySziYJfsAnW2MHKRo98vbAPiRDSk8")).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetTokenLargestAccounts() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getTokenLargestAccounts(PublicKey("2tWC4JAdL4AxEFJySziYJfsAnW2MHKRo98vbAPiRDSk8")).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetSlotLeaders() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val slot = solana.api.getSlot().blockingGet()
        val result = solana.api.getSlotLeaders(slot, 10).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetIdentity() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getIdentity().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetInflationReward() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getInflationReward(listOf(PublicKey("5U3bH5b6XtG99aVWLqwVzYPVpQiFHytBD68Rz2eFPZd7"))).blockingGet()
        Assert.assertNotNull(result)
    }

    /*
    TODO: Fix when  data is null
    @Test
    fun TestGetProgramAccounts() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getProgramAccounts(PublicKey("SwaPpA9LAaLfeLi3a68M4DjnLqgtticKg6CnyNwgAC8"), TokenSwapInfo::class.java).blockingGet()
        Assert.assertNotNull(result)
    }
    */


    @Test
    fun TestGetBlock() {
        val solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))
        val slot = solana.api.getSlot().blockingGet()
        val result = solana.api.getBlock(slot.toInt()).blockingGet()
        Assert.assertNotNull(result)
    }
}