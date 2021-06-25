package com.solana.rxsolana.api

import com.solana.Solana
import com.solana.core.PublicKey
import com.solana.networking.NetworkingRouter
import com.solana.networking.RPCEndpoint
import com.solana.rxsolana.api.*
import org.junit.Assert
import org.junit.Test
import java.util.*
import java.util.Map
import kotlin.collections.listOf
import kotlin.collections.mapOf

class Methods {
    @Test
    fun TestGetRecentBlockhash() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getRecentBlockhash().blockingGet()
        Assert.assertNotNull(result)
    }
    @Test
    fun TestGetBalance() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getBalance(PublicKey("AaXs7cLGcSVAsEt8QxstVrqhLhYN2iGhFNRemwYnHitV")).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetConfirmedTransaction() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getConfirmedTransaction("7Zk9yyJCXHapoKyHwd8AzPeW9fJWCvszR6VAcHUhvitN5W9QG9JRnoYXR8SBQPTh27piWEmdybchDt5j7xxoUth").blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetVoteAccounts() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getVoteAccounts().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetStakeActivation() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getStakeActivation(PublicKey("HDDhNo3H2t3XbLmRswHdTu5L8SvSMypz9UVFu68Wgmaf")).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetStakeActivationEpoch() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getStakeActivation(PublicKey("HDDhNo3H2t3XbLmRswHdTu5L8SvSMypz9UVFu68Wgmaf"), 143).blockingGet()
        Assert.assertNotNull(result)
    }

    /*@Test
    Doesnt work on CI
    fun TestRequestAirdrop() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.requestAirdrop(PublicKey("AaXs7cLGcSVAsEt8QxstVrqhLhYN2iGhFNRemwYnHitV"), 1010).blockingGet()
        Assert.assertNotNull(result)
    }*/

    @Test
    fun TestGetMinimumBalanceForRentExemption() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getMinimumBalanceForRentExemption(32000).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetBlockTime() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getBlockTime(63426807).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetAccountInfo() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getAccountInfo(PublicKey("AaXs7cLGcSVAsEt8QxstVrqhLhYN2iGhFNRemwYnHitV")).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetBlockHeight() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getBlockHeight().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetVersion() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getVersion().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestMinimumLedgerSlot() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.minimumLedgerSlot().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetFeeCalculatorForBlockhash() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getFeeCalculatorForBlockhash("3pkUeCqmzESag2V2upuvxsFqbAmejBerWNMCSvUTeTQt").blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetBlockCommitment() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getBlockCommitment(82493733).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetFeeRateGovernor() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getFeeRateGovernor().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetFees() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getFees().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetTransactionCount() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getTransactionCount().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetMaxRetransmitSlot() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getMaxRetransmitSlot().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetSupply() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getSupply().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetFirstAvailableBlock() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getFirstAvailableBlock().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetGenesisHash() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getGenesisHash().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetEpochInfo() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getEpochInfo().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetEpochSchedule() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getEpochSchedule().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetConfirmedBlock() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getConfirmedBlock(64201119).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetSnapshotSlot() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getSnapshotSlot().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetMaxShredInsertSlot() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getMaxShredInsertSlot().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetSlot() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getSlot().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetConfirmedBlocks() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getConfirmedBlocks(64203570, 64203580).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetSplTokenAccountInfo() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getSplTokenAccountInfo(PublicKey("EJwZgeZrdC8TXTQbQBoL6bfuAnFUUy1PVCMB4DYPzVaS")).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetTokenAccountsByOwner() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getTokenAccountsByOwner(PublicKey("AaXs7cLGcSVAsEt8QxstVrqhLhYN2iGhFNRemwYnHitV"), PublicKey("EJwZgeZrdC8TXTQbQBoL6bfuAnFUUy1PVCMB4DYPzVaS")).blockingGet()
        Assert.assertNotNull(result)
    }

    /*@Test
    fun TestGetTokenAccountsByDelegate() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val requiredParams = Map.of<String, Any>("mint", "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v")
        val result = solana.api.getTokenAccountsByDelegate(PublicKey.valueOf(
            "AoUnMozL1ZF4TYyVJkoxQWfjgKKtu8QUK9L4wFdEJick"), requiredParams , mapOf()).blockingGet()
        Assert.assertNotNull(result)
    }*/

    @Test
    fun TestGetSlotLeader() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getSlotLeader().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetClusterNodes() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getClusterNodes().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetTokenAccountBalance() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getTokenAccountBalance(PublicKey("FzhfekYF625gqAemjNZxjgTZGwfJpavMZpXCLFdypRFD")).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetConfirmedSignaturesForAddress2() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getConfirmedSignaturesForAddress2(PublicKey("5Zzguz4NsSRFxGkHfM4FmsFpGZiCDtY72zH2jzMcqkJx"), 10, null, null).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetTokenSupply() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getTokenSupply(PublicKey("2tWC4JAdL4AxEFJySziYJfsAnW2MHKRo98vbAPiRDSk8")).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetTokenLargestAccounts() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getTokenLargestAccounts(PublicKey("2tWC4JAdL4AxEFJySziYJfsAnW2MHKRo98vbAPiRDSk8")).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetSlotLeaders() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getSlotLeaders(64203570, 10).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetIdentity() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getIdentity().blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetInflationReward() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getInflationReward(listOf(PublicKey("5U3bH5b6XtG99aVWLqwVzYPVpQiFHytBD68Rz2eFPZd7"))).blockingGet()
        Assert.assertNotNull(result)
    }

    @Test
    fun TestGetProgramAccounts() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getProgramAccounts(PublicKey("SwaPpA9LAaLfeLi3a68M4DjnLqgtticKg6CnyNwgAC8")).blockingGet()
        Assert.assertNotNull(result)
    }

    /*
    Not available yet
    @Test
    fun TestGetBlock() {
        val solana = Solana(NetworkingRouter(RPCEndpoint.devnetSolana))
        val result = solana.api.getBlock(64199425).blockingGet()
        Assert.assertNotNull(result)
    }
    */
}