package com.solana.rxsolana.api

import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.models.*
import com.solana.networking.NetworkingRouter
import com.solana.networking.models.RpcResultTypes
import org.java_websocket.util.Base64
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


data class ApiError(override val message: String?) : Exception(message)

class Api(private val router: NetworkingRouter) {
    fun getBalance(account: PublicKey, onComplete: ((Result<Long>) -> Unit)) {
        val params: MutableList<Any> = ArrayList()
        params.add(account.toString())
        return router.call("getBalance", params, RpcResultTypes.ValueLong::class.java){ result ->
            result.onSuccess {
                onComplete(Result.success(it.value))
                return@call
            }.onFailure {
                onComplete(Result.failure(RuntimeException(it)))
                return@call
            }
        }
    }

    fun getRecentBlockhash(onComplete: ((Result<String>) -> Unit)) {
        return router.call("getRecentBlockhash", null, RecentBlockhash::class.java){ result ->
            result.onSuccess { recentBlockHash ->
                onComplete(Result.success(recentBlockHash.value.blockhash))
                return@call
            }.onFailure {
                onComplete(Result.failure(RuntimeException(it)))
                return@call
            }
        }
    }

    fun sendTransaction(
        transaction: Transaction,
        signer: Account,
        recentBlockHash: String?,
        onComplete: ((Result<String>) -> Unit)
    ) {
        return sendTransaction(transaction, listOf(signer), recentBlockHash, onComplete)
    }

    fun sendTransaction(transaction: Transaction, signer: Account, onComplete: ((Result<String>) -> Unit)) {
        return sendTransaction(transaction, listOf(signer), null, onComplete)
    }

    private fun sendTransaction(
        transaction: Transaction,
        signers: List<Account>,
        recentBlockHash: String?,
        onComplete: ((Result<String>) -> Unit)
    ) {
        if (recentBlockHash == null) {
            getRecentBlockhash { result ->
                result.onSuccess { recentBlockHash ->
                    transaction.setRecentBlockHash(recentBlockHash)
                    transaction.sign(signers)
                    val serializedTransaction: ByteArray = transaction.serialize()
                    val base64Trx: String = Base64.encodeBytes(serializedTransaction)
                    val params: MutableList<Any> = ArrayList()
                    params.add(base64Trx)
                    params.add(RpcSendTransactionConfig())
                    router.call("sendTransaction", params, String::class.java, onComplete)
                    return@getRecentBlockhash
                }.onFailure {
                    onComplete(Result.failure(RuntimeException(it)))
                    return@getRecentBlockhash
                }
            }
        } else {
            transaction.setRecentBlockHash(recentBlockHash)
            transaction.sign(signers)
            val serializedTransaction: ByteArray = transaction.serialize()
            val base64Trx: String = Base64.encodeBytes(serializedTransaction)
            val params: MutableList<Any> = ArrayList()
            params.add(base64Trx)
            params.add(RpcSendTransactionConfig())
            router.call("sendTransaction", params, String::class.java, onComplete)
            return
        }
    }

    fun getConfirmedTransaction(signature: String,
                                onComplete: ((Result<ConfirmedTransaction>) -> Unit)
    ){
        val params: MutableList<Any> = ArrayList()
        params.add(signature)
        // TODO jsonParsed, base58, base64
        // the default encoding is JSON
        // params.add("json");
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

    /**
     * Returns identity and transaction information about a confirmed block in the ledger
     */
    fun getBlock(slot: Int, onComplete: ((Result<Block>) -> Unit)) {
        val params: MutableList<Any> = ArrayList()
        params.add(slot)
        params.add(BlockConfig())
        router.call("getBlock", params, Block::class.java, onComplete)
    }


    /**
     * Returns information about the current epoch
     * @return
     * @throws ApiError
     */
    fun getEpochInfo(onComplete: ((Result<EpochInfo>) -> Unit)) {
        val params: List<Any> = ArrayList()
        router.call("getEpochInfo", params, EpochInfo::class.java, onComplete)
    }

    fun getEpochSchedule(onComplete: ((Result<EpochSchedule>) -> Unit)) {
        val params: List<Any> = ArrayList()
        router.call("getEpochSchedule", params, EpochSchedule::class.java, onComplete)
    }

    /**
     * Returns identity and transaction information about a confirmed block in the ledger
     * DEPRECATED: use getBlock instead
     */
    @Deprecated("")
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

    /**
     * Returns a list of confirmed blocks between two slots
     * DEPRECATED: use getBlocks instead
     */
    @Deprecated("")
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

    /**
     * Returns a list of confirmed blocks between two slots
     * DEPRECATED: use getBlocks instead
     */
    @Deprecated("")
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

    /*@Throws(ApiError::class)
    fun getConfirmedSignaturesForAddress2(
        account: PublicKey,
        limit: Int
    ): List<SignatureInformation>? {
        val params: MutableList<Any> = ArrayList()
        params.add(account.toString())
        params.add(ConfirmedSignFAddr2(limit.toLong()))
        val rawResult: List<AbstractMap> = client.call(
            "getConfirmedSignaturesForAddress2", params,
            MutableList::class.java
        )
        val result: MutableList<SignatureInformation> = ArrayList()
        for (item in rawResult) {
            result.add(SignatureInformation(item))
        }
        return result
    }

    @Throws(ApiError::class)
    fun getProgramAccounts(
        account: PublicKey,
        offset: Long,
        bytes: String?
    ): List<ProgramAccount?>? {
        val filters: MutableList<Any> = ArrayList()
        filters.add(Filter(Memcmp(offset, bytes)))
        val programAccountConfig = ProgramAccountConfig(filters)
        return getProgramAccounts(account, programAccountConfig)
    }

    @Throws(ApiError::class)
    fun getProgramAccounts(account: PublicKey?): List<ProgramAccount?>? {
        return getProgramAccounts(account, ProgramAccountConfig(Encoding.base64))
    }

    @Throws(ApiError::class)
    fun getProgramAccounts(
        account: PublicKey,
        programAccountConfig: ProgramAccountConfig?
    ): List<ProgramAccount?>? {
        val params: MutableList<Any> = ArrayList()
        params.add(account.toString())
        if (programAccountConfig != null) {
            params.add(programAccountConfig)
        }
        val rawResult: List<AbstractMap> = client.call(
            "getProgramAccounts", params,
            MutableList::class.java
        )
        val result: MutableList<ProgramAccount?> = ArrayList()
        for (item in rawResult) {
            result.add(ProgramAccount(item))
        }
        return result
    }

    @Throws(ApiError::class)
    fun getProgramAccounts(
        account: PublicKey,
        memcmpList: List<Memcmp?>,
        dataSize: Int
    ): List<ProgramAccount>? {
        val params: MutableList<Any> = ArrayList()
        params.add(account.toString())
        val filters: MutableList<Any> = ArrayList()
        memcmpList.forEach(Consumer { memcmp: Memcmp? ->
            filters.add(
                Filter(
                    memcmp
                )
            )
        })
        filters.add(DataSize(dataSize.toLong()))
        val programAccountConfig = ProgramAccountConfig(filters)
        params.add(programAccountConfig)
        val rawResult: List<AbstractMap> = client.call(
            "getProgramAccounts", params,
            MutableList::class.java
        )
        val result: MutableList<ProgramAccount> = ArrayList()
        for (item in rawResult) {
            result.add(ProgramAccount(item))
        }
        return result
    }

    @Throws(ApiError::class)
    fun getProgramAccounts(account: PublicKey, memcmpList: List<Memcmp?>): List<ProgramAccount>? {
        val params: MutableList<Any> = ArrayList()
        params.add(account.toString())
        val filters: MutableList<Any> = ArrayList()
        memcmpList.forEach(Consumer { memcmp: Memcmp? ->
            filters.add(
                Filter(
                    memcmp
                )
            )
        })
        val programAccountConfig = ProgramAccountConfig(filters)
        params.add(programAccountConfig)
        val rawResult: List<AbstractMap> = client.call(
            "getProgramAccounts", params,
            MutableList::class.java
        )
        val result: MutableList<ProgramAccount> = ArrayList()
        for (item in rawResult) {
            result.add(ProgramAccount(item))
        }
        return result
    }

    @Throws(ApiError::class)
    fun simulateTransaction(
        transaction: String,
        addresses: List<PublicKey?>
    ): SimulatedTransaction? {
        val simulateTransactionConfig =
            SimulateTransactionConfig(Encoding.base64)
        simulateTransactionConfig.setAccounts(
            java.util.Map.of(
                "encoding",
                Encoding.base64,
                "addresses",
                addresses.stream().map<Any>(PublicKey::toBase58).collect(Collectors.toList())
            )
        )
        simulateTransactionConfig.setReplaceRecentBlockhash(true)
        val params: MutableList<Any> = ArrayList()
        params.add(transaction)
        params.add(simulateTransactionConfig)
        return client.call(
            "simulateTransaction",
            params,
            SimulatedTransaction::class.java
        )
    }


    @Throws(ApiError::class)
    fun getClusterNodes(): List<ClusterNode>? {
        val params: List<Any> = ArrayList()

        // TODO - fix uncasted type stuff
        val rawResult: List<AbstractMap> = client.call(
            "getClusterNodes", params,
            MutableList::class.java
        )
        val result: MutableList<ClusterNode> = ArrayList()
        for (item in rawResult) {
            result.add(ClusterNode(item))
        }
        return result
    }

    @Throws(ApiError::class)
    fun getTokenAccountsByOwner(owner: PublicKey, tokenMint: PublicKey): PublicKey? {
        val params: MutableList<Any> = ArrayList()
        params.add(owner.toBase58())
        val parameterMap: MutableMap<String, Any> = HashMap()
        parameterMap["mint"] = tokenMint.toBase58()
        params.add(parameterMap)
        val rawResult: Map<String, Any> = client.call(
            "getTokenAccountsByOwner", params,
            MutableMap::class.java
        )
        val tokenAccountKey: PublicKey
        try {
            val base58 = ((rawResult["value"] as List<*>?)!![0] as Map<*, *>)["pubkey"] as String?
            tokenAccountKey = PublicKey(base58)
        } catch (ex: java.lang.Exception) {
            throw ApiError("unable to get token account by owner")
        }
        return tokenAccountKey
    }



    @Throws(ApiError::class)
    fun getInflationReward(
        addresses: List<PublicKey?>,
        epoch: Long?,
        commitment: String?
    ): List<InflationReward?>? {
        val params: MutableList<Any> = ArrayList()
        params.add(addresses.stream().map<Any>(PublicKey::toString).collect(Collectors.toList()))
        if (null != epoch) {
            val rpcEpochConfig: RpcEpochConfig
            rpcEpochConfig = commitment?.let { RpcEpochConfig(epoch, it) } ?: RpcEpochConfig(epoch)
            params.add(rpcEpochConfig)
        }
        val rawResult: List<AbstractMap> = client.call(
            "getInflationReward", params,
            MutableList::class.java
        )
        val result: MutableList<InflationReward?> = ArrayList()
        for (item in rawResult) {
            result.add(InflationReward(item))
        }
        return result
    }

    @Throws(ApiError::class)
    fun getSlotLeaders(startSlot: Long, limit: Long): List<PublicKey>? {
        val params: MutableList<Any> = ArrayList()
        params.add(startSlot)
        params.add(limit)
        val rawResult: List<String> = client.call(
            "getSlotLeaders", params,
            MutableList::class.java
        )
        val result: MutableList<PublicKey> = ArrayList()
        for (item in rawResult) {
            result.add(PublicKey(item))
        }
        return result
    }



    @Throws(ApiError::class)
    fun getIdentity(): PublicKey? {
        val rawResult: Map<String, Any> = client.call(
            "getIdentity", ArrayList(),
            MutableMap::class.java
        )
        val identity: PublicKey
        try {
            val base58 = rawResult["identity"] as String?
            identity = PublicKey(base58)
        } catch (ex: java.lang.Exception) {
            throw ApiError("unable to get identity")
        }
        return identity
    }

    @Throws(ApiError::class)
    fun getTokenAccountBalance(tokenAccount: PublicKey): TokenAmountInfo? {
        val params: MutableList<Any> = ArrayList()
        params.add(tokenAccount.toString())
        val rawResult: Map<String, Any> = client.call(
            "getTokenAccountBalance", params,
            MutableMap::class.java
        )
        return TokenAmountInfo(rawResult["value"] as AbstractMap?)
    }

    @Throws(ApiError::class)
    fun getTokenSupply(tokenMint: PublicKey): TokenAmountInfo? {
        val params: MutableList<Any> = ArrayList()
        params.add(tokenMint.toString())
        val rawResult: Map<String, Any> = client.call(
            "getTokenSupply", params,
            MutableMap::class.java
        )
        return TokenAmountInfo(rawResult["value"] as AbstractMap?)
    }

    @Throws(ApiError::class)
    fun getTokenLargestAccounts(tokenMint: PublicKey): List<TokenAccount>? {
        val params: MutableList<Any> = ArrayList()
        params.add(tokenMint.toString())
        val rawResult: Map<String, Any> = client.call(
            "getTokenLargestAccounts", params,
            MutableMap::class.java
        )
        val result: MutableList<TokenAccount> = ArrayList()
        for (item in (rawResult["value"] as List<AbstractMap?>?)!!) {
            result.add(TokenAccount(item))
        }
        return result
    }

    @Throws(ApiError::class)
    fun getTokenAccountsByOwner(
        accountOwner: PublicKey, requiredParams: Map<String, Any>,
        optionalParams: Map<String, Any>?
    ): TokenAccountInfo? {
        return getTokenAccount(
            accountOwner,
            requiredParams,
            optionalParams,
            "getTokenAccountsByOwner"
        )
    }

    @Throws(ApiError::class)
    fun getTokenAccountsByDelegate(
        accountDelegate: PublicKey, requiredParams: Map<String, Any>,
        optionalParams: Map<String, Any>?
    ): TokenAccountInfo? {
        return getTokenAccount(
            accountDelegate,
            requiredParams,
            optionalParams,
            "getTokenAccountsByDelegate"
        )
    }



    */
}
