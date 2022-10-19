package com.solana.networking.socket

import com.solana.core.PublicKeyJsonAdapter
import com.solana.core.PublicKeyRule
import com.solana.models.ProgramAccount
import com.solana.models.buffer.*
import com.solana.models.buffer.moshi.AccountInfoJsonAdapter
import com.solana.models.buffer.moshi.MintJsonAdapter
import com.solana.models.buffer.moshi.TokenSwapInfoJsonAdapter
import com.solana.networking.RPCEndpoint
import com.solana.networking.models.RpcRequest
import com.solana.networking.models.RpcResponse
import com.solana.networking.socket.models.*
import com.solana.vendor.borshj.Borsh
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import okio.ByteString

sealed class SolanaSocketError: Exception() {
    object disconnected: SolanaSocketError()
    object couldNotSerialize: SolanaSocketError()
    object couldNotWrite: SolanaSocketError()
}

interface SolanaSocketEventsDelegate {
    fun connected()
    fun accountNotification(notification: RpcResponse<BufferInfo<AccountInfoData>>)
    fun programNotification(notification: RpcResponse<ProgramAccount<AccountInfoData>>)
    fun signatureNotification(notification: RpcResponse<SignatureNotification>)
    fun logsNotification(notification: RpcResponse<LogsNotification>)
    fun unsubscribed(id: String)
    fun subscribed(socketId: Int, id: String)
    fun disconnecting(code: Int, reason: String)
    fun disconnected(code: Int, reason: String)
    fun error(error: Exception)
}

class SolanaSocket(
    private val endpoint: RPCEndpoint,
    private val client: OkHttpClient = OkHttpClient(),
    val enableDebugLogs: Boolean = false
): WebSocketListener() {
    private val TAG = "SOLANA_SOCKET"
    private var socket: WebSocket? = null
    private var delegate: SolanaSocketEventsDelegate? = null

    private fun borsh(): Borsh {
        val borsh = Borsh()
        borsh.setRules(listOf(PublicKeyRule(), AccountInfoRule(), MintRule(), TokenSwapInfoRule()))
        return borsh
    }
    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(PublicKeyJsonAdapter())
            .add(MintJsonAdapter(borsh()))
            .add(TokenSwapInfoJsonAdapter(borsh()))
            .add(AccountInfoJsonAdapter(borsh()))
            .addLast(KotlinJsonAdapterFactory()).build()
    }

    fun start(delegate: SolanaSocketEventsDelegate) {
        this.delegate = delegate
        val request: Request = Request.Builder().url(endpoint.urlWebSocket).build()
        socket = client.newWebSocket(request, this)
        client.dispatcher.executorService.shutdown()
    }

    fun stop(){
        socket?.cancel()
    }

    fun accountSubscribe(publicKey: String): Result<String> {
        val params: MutableList<Any> = ArrayList()
        params.add(publicKey)
        params.add(mapOf("encoding" to "base64", "commitment" to "recent"))
        val rpcRequest = RpcRequest(SocketMethod.accountSubscribe.string, params)
        return writeToSocket(rpcRequest)
    }

    fun accountUnSubscribe(socketId: Int): Result<String> {
        val params: MutableList<Any> = ArrayList()
        params.add(socketId)
        val rpcRequest = RpcRequest(SocketMethod.accountUnsubscribe.string, params)
        return writeToSocket(rpcRequest)
    }

    fun logsSubscribe(mentions: List<String>): Result<String> {
        val params: MutableList<Any> = ArrayList()
        params.add(mapOf("mentions" to mentions))
        params.add(mapOf("encoding" to "base64", "commitment" to "recent"))
        val rpcRequest = RpcRequest(SocketMethod.logsSubscribe.string, params)
        return writeToSocket(rpcRequest)
    }

    fun logsSubscribeAll(): Result<String> {
        val params: MutableList<Any> = ArrayList()
        params.add("all")
        params.add(mapOf("encoding" to "base64", "commitment" to "recent"))
        val rpcRequest = RpcRequest(SocketMethod.logsSubscribe.string, params)
        return writeToSocket(rpcRequest)
    }

    fun logsUnsubscribe(socketId: Int): Result<String> {
        val params: MutableList<Any> = ArrayList()
        params.add(socketId)
        val rpcRequest = RpcRequest(SocketMethod.logsUnsubscribe.string, params)
        return writeToSocket(rpcRequest)
    }

    fun programSubscribe(publicKey: String): Result<String> {
        val params: MutableList<Any> = ArrayList()
        params.add(publicKey)
        params.add(mapOf("encoding" to "base64", "commitment" to "recent"))
        val rpcRequest = RpcRequest(SocketMethod.programSubscribe.string, params)
        return writeToSocket(rpcRequest)
    }

    fun programUnsubscribe(socketId: Int): Result<String> {
        val params: MutableList<Any> = ArrayList()
        params.add(socketId)
        val rpcRequest = RpcRequest(SocketMethod.programUnsubscribe.string, params)
        return writeToSocket(rpcRequest)
    }

    fun signatureSubscribe(signature: String): Result<String> {
        val params: MutableList<Any> = ArrayList()
        params.add(signature)
        params.add(mapOf("encoding" to "base64", "commitment" to "recent"))
        val rpcRequest = RpcRequest(SocketMethod.signatureSubscribe.string, params)
        return writeToSocket(rpcRequest)
    }

    fun signatureUnsubscribe(socketId: Int): Result<String> {
        val params: MutableList<Any> = ArrayList()
        params.add(socketId)
        val rpcRequest = RpcRequest(SocketMethod.signatureUnsubscribe.string, params)
        return writeToSocket(rpcRequest)
    }

    fun writeToSocket(request: RpcRequest): Result<String> {
        val rpcRequestJsonAdapter = moshi.adapter(RpcRequest::class.java)
        val json = rpcRequestJsonAdapter.toJson(request)
        if(socket?.send(json) != true){ return Result.failure(SolanaSocketError.couldNotWrite) }
        return Result.success(request.id)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        if(enableDebugLogs) { println(TAG + " connected")}
        delegate?.connected()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {

        if(enableDebugLogs) { println(TAG + " text: $text")}
        val responseJsonAdapter = moshi.adapter(Map::class.java)
        try {
            val dictJson = responseJsonAdapter.fromJson(text)
            val methodString = dictJson?.get("method")
            methodString?.let {
                when(it){
                    SocketMethod.accountNotification.string -> {
                        val subscriptionAdapter: JsonAdapter<RpcResponse<BufferInfo<AccountInfoData>>> = moshi.adapter(
                            Types.newParameterizedType(
                                RpcResponse::class.java,
                                Types.newParameterizedType(
                                    BufferInfo::class.java,
                                    AccountInfoData::class.java
                                )
                            )
                        )
                        subscriptionAdapter.fromJson(text)?.let {
                            delegate?.accountNotification(it)
                        }
                    }
                    SocketMethod.signatureNotification.string -> {
                        val subscriptionAdapter: JsonAdapter<RpcResponse<SignatureNotification>> = moshi.adapter(
                            Types.newParameterizedType(
                                RpcResponse::class.java,
                                SignatureNotification::class.java
                            )
                        )
                        subscriptionAdapter.fromJson(text)?.let {
                            delegate?.signatureNotification(it)
                        }
                    }
                    SocketMethod.logsNotification.string -> {
                        val subscriptionAdapter: JsonAdapter<RpcResponse<LogsNotification>> = moshi.adapter(
                            Types.newParameterizedType(
                                RpcResponse::class.java,
                                LogsNotification::class.java
                            )
                        )
                        subscriptionAdapter.fromJson(text)?.let {
                            delegate?.logsNotification(it)
                        }
                    }
                    SocketMethod.programNotification.string -> {
                        val subscriptionAdapter: JsonAdapter<RpcResponse<ProgramAccount<AccountInfoData>>> = moshi.adapter(
                            Types.newParameterizedType(
                                RpcResponse::class.java,
                                Types.newParameterizedType(
                                    ProgramAccount::class.java,
                                    AccountInfoData::class.java
                                )
                            )
                        )
                        subscriptionAdapter.fromJson(text)?.let {
                            delegate?.programNotification(it)
                        }

                    }
                    else -> { }
                }
            } ?: run {
                if(dictJson?.get("result") is Double) {
                    val subscriptionAdapter: JsonAdapter<RpcResponse<Int>> = moshi.adapter(
                        Types.newParameterizedType(
                            RpcResponse::class.java,
                            Int::class.javaObjectType
                        )
                    )
                    subscriptionAdapter.fromJson(text)?.let {
                        delegate?.subscribed(it.result!!, it.id!!)
                    }
                }

                if(responseJsonAdapter.fromJson(text)?.get("result") is Boolean){
                    val unSubscriptionAdapter: JsonAdapter<RpcResponse<Boolean>> = moshi.adapter(
                        Types.newParameterizedType(
                            RpcResponse::class.java,
                            Boolean::class.javaObjectType
                        )
                    )
                    unSubscriptionAdapter.fromJson(text)?.let {
                        delegate?.unsubscribed(it.id!!)
                    }
                }
            }

        } catch (error: java.lang.Exception){
            delegate?.error(error)
        }
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        if(enableDebugLogs) { println(TAG + " bytes")}

    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        if(enableDebugLogs) { println(TAG + " closing")}
        delegate?.disconnecting(code, reason)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        if(enableDebugLogs) { println(TAG + " closed")}
        delegate?.disconnected(code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        if(enableDebugLogs) { println(TAG + " failure")}
        delegate?.error(Exception(t))
    }
}