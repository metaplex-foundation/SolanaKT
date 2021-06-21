package com.solana.api

import android.os.Build
import androidx.annotation.RequiresApi
import com.solana.models.*
import com.solana.models.ConfigObjects.*
import com.solana.models.RpcSendTransactionConfig.Encoding
import com.solana.networking.NetworkingRouter
import com.solana.networking.models.RpcResultTypes.ValueLong
import com.solana.socket.NotificationEventListener
import com.solana.socket.SubscriptionWebSocketClient
import java.util.*
import kotlin.collections.ArrayList


class RpcException(message: String?) : Exception(message) {
    companion object {
        private const val serialVersionUID = 8315999767009642193L
    }
}

public class Api(private val router: NetworkingRouter) {

    @Throws(RpcException::class)
    fun getRecentBlockhash(): String {
        return router.call("getRecentBlockhash", null, RecentBlockhash::class.java).recentBlockhash!!
    }
    @Throws(RpcException::class)
    fun sendTransaction(transaction: Transaction, signer: Account): String {
        return sendTransaction(transaction, Arrays.asList(signer))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(RpcException::class)
    fun sendTransaction(transaction: Transaction, signers: List<Account>): String {
        val recentBlockhash: String = getRecentBlockhash()
        transaction.setRecentBlockHash(recentBlockhash)
        transaction.sign(signers)
        val serializedTransaction: ByteArray = transaction.serialize()
        val base64Trx: String = Base64.getEncoder().encodeToString(serializedTransaction)
        val params: MutableList<Any> = ArrayList()
        params.add(base64Trx)
        params.add(RpcSendTransactionConfig())
        return router.call("sendTransaction", params, String::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(RpcException::class)
    fun sendAndConfirmTransaction(
        transaction: Transaction, signers: List<Account>,
        listener: NotificationEventListener
    ) {
        val signature = sendTransaction(transaction, signers)
        val subClient: SubscriptionWebSocketClient =
            SubscriptionWebSocketClient.getInstance(router.endpoint.url.toString())!!
        subClient.signatureSubscribe(signature, listener)
    }

    @Throws(RpcException::class)
    fun getBalance(account: PublicKey): Long {
        val params: MutableList<Any> = ArrayList()
        params.add(account.toString())
        return router.call("getBalance", params, ValueLong::class.java).value
    }

    @Throws(RpcException::class)
    fun getConfirmedTransaction(signature: String): ConfirmedTransaction {
        val params: MutableList<Any> = ArrayList()
        params.add(signature)
        // TODO jsonParsed, base58, base64
        // the default encoding is JSON
        // params.add("json");
        return router.call("getConfirmedTransaction", params, ConfirmedTransaction::class.java)
    }

    @Throws(RpcException::class)
    fun getConfirmedSignaturesForAddress2(
        account: PublicKey,
        limit: Int
    ): List<SignatureInformation>? {
        val params: MutableList<Any> = ArrayList()
        params.add(account.toString())
        params.add(ConfirmedSignFAddr2(limit))
        val rawResult = router.call(
            "getConfirmedSignaturesForAddress2", params,
            List::class.java
        )
        val result: MutableList<SignatureInformation> = ArrayList()
        for (item in rawResult) {
            result.add(SignatureInformation(item as Map<String, Any>))
        }
        return result
    }

    @Throws(RpcException::class)
    fun getProgramAccounts(
        account: PublicKey,
        offset: Long,
        bytes: String?
    ): List<ProgramAccount?> {
        val filters: MutableList<Any> = ArrayList()
        filters.add(Filter(Memcmp(offset, bytes)))
        val programAccountConfig = ProgramAccountConfig(filters)
        return getProgramAccounts(account, programAccountConfig)
    }

    @Throws(RpcException::class)
    fun getProgramAccounts(account: PublicKey): List<ProgramAccount?>? {
        return getProgramAccounts(account, ProgramAccountConfig(Encoding.base64))
    }

    @Throws(RpcException::class)
    fun getProgramAccounts(
        account: PublicKey,
        programAccountConfig: ProgramAccountConfig?
    ): List<ProgramAccount?> {
        val params: ArrayList<Any> = ArrayList()
        params.add(account.toString())

        if (programAccountConfig != null) {
            params.add(programAccountConfig)
        }

        val rawResult = router.call(
            "getProgramAccounts", params,
            List::class.java
        )

        val result: MutableList<ProgramAccount> = ArrayList()
        for (item in rawResult) {
            result.add(ProgramAccount(item as Map<String, Any>))
        }

        return result
    }

    @Throws(RpcException::class)
    fun getAccountInfo(account: PublicKey): AccountInfo {
        val params: MutableList<Any> = ArrayList()
        params.add(account.toString())
        params.add(RpcSendTransactionConfig())
        return router.call("getAccountInfo", params, AccountInfo::class.java)
    }

    @Throws(RpcException::class)
    fun getMinimumBalanceForRentExemption(dataLength: Long): Long {
        val params: MutableList<Any> = ArrayList()
        params.add(dataLength)
        return router.call("getMinimumBalanceForRentExemption", params, Long::class.java)
    }

    @Throws(RpcException::class)
    fun getBlockTime(block: Long): Long {
        val params: MutableList<Any> = ArrayList()
        params.add(block)
        return router.call("getBlockTime", params, Long::class.java)
    }

    @Throws(RpcException::class)
    fun requestAirdrop(address: PublicKey, lamports: Long): String {
        val params: MutableList<Any> = ArrayList()
        params.add(address.toString())
        params.add(lamports)
        return router.call("requestAirdrop", params, String::class.java)
    }
}

