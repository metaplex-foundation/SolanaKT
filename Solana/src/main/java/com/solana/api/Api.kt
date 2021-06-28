package com.solana.api

import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.models.*
import com.solana.networking.NetworkingRouter
import org.java_websocket.util.Base64
import java.lang.RuntimeException
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


data class ApiError(override val message: String?) : Exception(message)

class Api(val router: NetworkingRouter) {

    fun sendTransaction(
        transaction: Transaction,
        signers: List<Account>,
        recentBlockHash: String? = null,
        onComplete: ((Result<String>) -> Unit)
    ) {
        if (recentBlockHash == null) {
            getRecentBlockhash { result ->
                result.map { recentBlockHash ->
                    transaction.setRecentBlockHash(recentBlockHash)
                    transaction.sign(signers)
                    val serializedTransaction: ByteArray = transaction.serialize()
                    val base64Trx: String = Base64.encodeBytes(serializedTransaction)
                    listOf(base64Trx, RpcSendTransactionConfig())
                }.onSuccess {
                    router.call("sendTransaction", it, String::class.java, onComplete)
                }.onFailure {
                    onComplete(Result.failure(RuntimeException(it)))
                }
            }
        } else {
            transaction.setRecentBlockHash(recentBlockHash)
            transaction.sign(signers)
            val serializedTransaction: ByteArray = transaction.serialize()
            val base64Trx: String = Base64.encodeBytes(serializedTransaction)
            val params = listOf(base64Trx, RpcSendTransactionConfig())
            router.call("sendTransaction", params, String::class.java, onComplete)
        }
    }

    fun getConfirmedTransaction(signature: String,
                                onComplete: ((Result<ConfirmedTransaction>) -> Unit)
    ){
        val params: MutableList<Any> = ArrayList()
        params.add(signature)
        return router.call("getConfirmedTransaction", params, ConfirmedTransaction::class.java, onComplete)
    }

    fun getVoteAccounts(onComplete: ((Result<VoteAccounts>) -> Unit)) {
        return getVoteAccounts(null, onComplete)
    }

    fun getVoteAccounts(votePubkey: PublicKey?, onComplete: ((Result<VoteAccounts>) -> Unit)) {
        val params: MutableList<Any> = ArrayList()
        if (votePubkey != null) {
            params.add(VoteAccountConfig(votePubkey.toBase58()))
        }
        router.call("getVoteAccounts", params, VoteAccounts::class.java, onComplete)
    }

    fun getStakeActivation(publicKey: PublicKey, onComplete: ((Result<StakeActivation>) -> Unit)) {
        val params: MutableList<Any> = ArrayList()
        params.add(publicKey.toBase58())
        router.call("getStakeActivation", params, StakeActivation::class.java, onComplete)
    }

    fun getStakeActivation(publicKey: PublicKey, epoch: Long, onComplete: ((Result<StakeActivation>) -> Unit)) {
        val params: MutableList<Any> = ArrayList()
        params.add(publicKey.toBase58())
        params.add(StakeActivationConfig(epoch))
        router.call("getStakeActivation", params, StakeActivation::class.java, onComplete)
    }

    fun getAccountInfo(account: PublicKey, onComplete: ((Result<AccountInfo>) -> Unit)) {
        return getAccountInfo(account, HashMap(), onComplete)
    }

    fun getAccountInfo(account: PublicKey, additionalParams: Map<String?, Any?>, onComplete: ((Result<AccountInfo>) -> Unit)) {
        val params: MutableList<Any> = ArrayList()
        val parameterMap: MutableMap<String, Any?> = HashMap()
        parameterMap["commitment"] = additionalParams.getOrDefault("commitment", "max")
        parameterMap["encoding"] = additionalParams.getOrDefault("encoding", "base64")

        if (additionalParams.containsKey("dataSlice")) {
            parameterMap["dataSlice"] = additionalParams["dataSlice"]
        }
        params.add(account.toString())
        params.add(parameterMap)
        router.call("getAccountInfo", params, AccountInfo::class.java, onComplete)
    }

    fun requestAirdrop(address: PublicKey, lamports: Long, onComplete: ((Result<String>) -> Unit)) {
        val params: MutableList<Any> = ArrayList()
        params.add(address.toString())
        params.add(lamports)
        router.call("requestAirdrop", params, String::class.java, onComplete)
    }

    fun getMinimumBalanceForRentExemption(dataLength: Long,  onComplete: ((Result<Long>) -> Unit)) {
        val params: MutableList<Any> = ArrayList()
        params.add(dataLength)
        router.call("getMinimumBalanceForRentExemption", params, Long::class.javaObjectType, onComplete)
    }

    fun getBlockTime(block: Long, onComplete: ((Result<Long>) -> Unit)) {
        val params: MutableList<Any> = ArrayList()
        params.add(block)
        router.call("getBlockTime", params, Long::class.javaObjectType, onComplete)
    }

    fun getBlockHeight(onComplete: ((Result<Long>) -> Unit)) {
        router.call("getBlockHeight", ArrayList(), Long::class.javaObjectType, onComplete)
    }

    fun minimumLedgerSlot(onComplete: ((Result<Long>) -> Unit)) {
        router.call("minimumLedgerSlot", ArrayList(), Long::class.javaObjectType, onComplete)
    }

    fun getVersion(onComplete: ((Result<SolanaVersion>) -> Unit)) {
        router.call("getVersion", ArrayList(), SolanaVersion::class.java, onComplete)
    }

    fun getBlockCommitment(block: Long, onComplete: ((Result<BlockCommitment>) -> Unit)) {
        val params: MutableList<Any> = ArrayList()
        params.add(block)
        router.call("getBlockCommitment", params, BlockCommitment::class.java, onComplete)
    }

    fun getFeeCalculatorForBlockhash(blockhash: String, onComplete: ((Result<FeeCalculatorInfo>) -> Unit)) {
        val params: MutableList<Any> = ArrayList()
        params.add(blockhash)
        router.call("getFeeCalculatorForBlockhash", params, FeeCalculatorInfo::class.java, onComplete)
    }

    fun getFeeRateGovernor(onComplete: ((Result<FeeRateGovernorInfo>) -> Unit)) {
        router.call("getFeeRateGovernor", ArrayList(), FeeRateGovernorInfo::class.java, onComplete)
    }

    fun getFees(onComplete: ((Result<FeesInfo>) -> Unit)){
        router.call("getFees", ArrayList(), FeesInfo::class.java, onComplete)
    }

    fun getTransactionCount(onComplete: ((Result<Long>) -> Unit)) {
        router.call("getTransactionCount", ArrayList(), Long::class.javaObjectType, onComplete)
    }

    fun getMaxRetransmitSlot(onComplete: ((Result<Long>) -> Unit)) {
        router.call("getMaxRetransmitSlot", ArrayList(), Long::class.javaObjectType, onComplete)
    }

    fun getSupply(onComplete: ((Result<Supply>) -> Unit)){
        router.call("getSupply", ArrayList(), Supply::class.java, onComplete)
    }

    fun getFirstAvailableBlock(onComplete: ((Result<Long>) -> Unit)){
        router.call("getFirstAvailableBlock", ArrayList(), Long::class.javaObjectType, onComplete)
    }

    fun getGenesisHash(onComplete: ((Result<String>) -> Unit)){
        router.call("getGenesisHash", ArrayList(), String::class.java, onComplete)
    }

    fun getBlock(slot: Int, onComplete: ((Result<Block>) -> Unit)) {
        val params: MutableList<Any> = ArrayList()
        params.add(slot)
        params.add(BlockConfig())
        router.call("getBlock", params, Block::class.java, onComplete)
    }

    fun getEpochInfo(onComplete: ((Result<EpochInfo>) -> Unit)) {
        val params: List<Any> = ArrayList()
        router.call("getEpochInfo", params, EpochInfo::class.java, onComplete)
    }

    fun getEpochSchedule(onComplete: ((Result<EpochSchedule>) -> Unit)) {
        val params: List<Any> = ArrayList()
        router.call("getEpochSchedule", params, EpochSchedule::class.java, onComplete)
    }

    fun getConfirmedBlock(slot: Int, onComplete: (Result<ConfirmedBlock>) -> Unit) {
        val params: MutableList<Any> = ArrayList()
        params.add(slot)
        params.add(BlockConfig())
        router.call("getConfirmedBlock", params, ConfirmedBlock::class.java, onComplete)
    }

    fun getSnapshotSlot(onComplete: (Result<Long>) -> Unit) {
        router.call("getSnapshotSlot", ArrayList(), Long::class.javaObjectType, onComplete)
    }

    fun getMaxShredInsertSlot(onComplete: (Result<Long>) -> Unit) {
        router.call("getMaxShredInsertSlot", ArrayList(), Long::class.javaObjectType, onComplete)
    }

    fun getSlot(onComplete: (Result<Long>) -> Unit) {
        router.call("getSlot", ArrayList(), Long::class.javaObjectType, onComplete)
    }

    fun getConfirmedBlocks(start: Int, end: Int?, onComplete: (Result<List<Double>>) -> Unit) {
        val params: List<Int>
        params = if (end == null) listOf(start) else listOf(start, end)
        router.call("getConfirmedBlocks", params, List::class.java) { result ->
            result.map { list ->
                list.map { it as Double }
            }.onSuccess {
                onComplete(Result.success(it))
            }.onFailure {
                onComplete(Result.failure(it))
            }
        }
    }

    fun getConfirmedBlocks(start: Int, onComplete: (Result<List<Double>>) -> Unit){
        this.getConfirmedBlocks(start, null, onComplete)
    }

    fun getSplTokenAccountInfo(account: PublicKey, onComplete: (Result<SplTokenAccountInfo>) -> Unit) {
        val params: MutableList<Any> = ArrayList()
        val parameterMap: MutableMap<String, Any> = HashMap()
        parameterMap["encoding"] = "jsonParsed"
        params.add(account.toString())
        params.add(parameterMap)
        router.call("getAccountInfo", params, SplTokenAccountInfo::class.java, onComplete)
    }

    fun getSlotLeader(onComplete: (Result<PublicKey>) -> Unit) {
        router.call("getSlotLeader", ArrayList(), String::class.java) { result ->
            result.map {
                PublicKey(it)
            }.onSuccess {
                onComplete(Result.success(it))
            }.onFailure {
                onComplete(Result.failure(it))
            }
        }
    }

    private fun getTokenAccount(
        account: PublicKey,
        requiredParams: Map<String, Any>,
        optionalParams: Map<String, Any>?,
        method: String,
        onComplete: (Result<TokenAccountInfo>) -> Unit
    ) {
        val params: MutableList<Any> = ArrayList()
        params.add(account.toString())

        // Either mint or programId is required
        var parameterMap: MutableMap<String?, Any?> = HashMap()
        if (requiredParams.containsKey("mint")) {
            parameterMap["mint"] = requiredParams["mint"].toString()
        } else if (requiredParams.containsKey("programId")) {
            parameterMap["programId"] = requiredParams["programId"].toString()
        } else {
            onComplete(Result.failure(ApiError("mint or programId are mandatory parameters")))
            return
        }
        params.add(parameterMap)
        if (null != optionalParams) {
            parameterMap = HashMap()
            parameterMap["commitment"] = optionalParams["commitment"] ?: "max"
            parameterMap["encoding"] = optionalParams["encoding"] ?: "jsonParsed"
            // No default for dataSlice
            if (optionalParams.containsKey("dataSlice")) {
                parameterMap["dataSlice"] = optionalParams["dataSlice"]
            }
            params.add(parameterMap)
        }
        router.call(method, params, TokenAccountInfo::class.java, onComplete)
    }

    fun getClusterNodes(onComplete: (Result<List<ClusterNode>>) -> Unit) {
        val params: List<Any> = ArrayList()
        router.call(
            "getClusterNodes", params,
            List::class.java
        ) { result ->   // List<AbstractMap>
            result.map {
                    it.filterNotNull()
                }.map { result ->
                    result.map { item -> item as Map<String, Any> }
                }.map {
                    val result: MutableList<ClusterNode> = ArrayList()
                    for (item in it) {
                        result.add(ClusterNode(item))
                    }
                    result
                }
                .onSuccess {
                onComplete(Result.success(it))
            }.onFailure {
                onComplete(Result.failure(it))
            }
        }
    }

    fun getTokenAccountBalance(tokenAccount: PublicKey,
                               onComplete: (Result<TokenResultObjects.TokenAmountInfo>) -> Unit)  {
        val params: MutableList<Any> = ArrayList()
        params.add(tokenAccount.toString())
        router.call(
            "getTokenAccountBalance",
            params,
            Map::class.java
        ){ result ->
            result.map {
                it["value"] as Map<String, Any>
            }.map {
                TokenResultObjects.TokenAmountInfo(it)
            }.onSuccess {
                onComplete(Result.success(it))
            }.onFailure {
                onComplete(Result.failure(it))
            }
        }
    }

    fun getTokenSupply(tokenMint: PublicKey,
                       onComplete: (Result<TokenResultObjects.TokenAmountInfo>) -> Unit) {
        val params: MutableList<Any> = ArrayList()
        params.add(tokenMint.toString())
        router.call(
            "getTokenSupply",
            params,
            Map::class.java
        ){ result ->
            result.map {
                it["value"]  as Map<String, Any>
            }.map {
                TokenResultObjects.TokenAmountInfo(it)
            }.onSuccess {
                onComplete(Result.success(it))
            }.onFailure {
                onComplete(Result.failure(it))
            }
        }
    }



    fun getTokenAccountsByOwner(
        accountOwner: PublicKey, requiredParams: Map<String, Any>,
        optionalParams: Map<String, Any>?,
        onComplete: (Result<TokenAccountInfo>) -> Unit,
    ) {
        getTokenAccount(
            accountOwner,
            requiredParams,
            optionalParams,
            "getTokenAccountsByOwner",
            onComplete
        )
    }

    fun getTokenAccountsByDelegate(
        accountDelegate: PublicKey,
        requiredParams: Map<String, Any>,
        optionalParams: Map<String, Any>?,
        onComplete: (Result<TokenAccountInfo>) -> Unit
    ) {
        return getTokenAccount(
            accountDelegate,
            requiredParams,
            optionalParams,
            "getTokenAccountsByDelegate",
            onComplete
        )
    }

    fun getTokenLargestAccounts(tokenMint: PublicKey,
                                onComplete: (Result<List<TokenResultObjects.TokenAccount>>) -> Unit) {
        val params: MutableList<Any> = ArrayList()
        params.add(tokenMint.toString())
        router.call(
            "getTokenLargestAccounts", params,
            Map::class.java
        ){ result ->
            result.map {
                it["value"] as List<*>
            }.map {
                it.map { item -> item as Map<String, Any> }
            }.map {
                val list: MutableList<TokenResultObjects.TokenAccount> = ArrayList()
                for (item in (it)) {
                    list.add(TokenResultObjects.TokenAccount(item))
                }
                list
            }.onSuccess {
                onComplete(Result.success(it))
            }.onFailure {
                onComplete(Result.failure(it))
            }
        }
    }
}
