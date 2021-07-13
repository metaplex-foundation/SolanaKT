package com.solana.networking.socket

import android.util.Log
import com.solana.core.PublicKeyRule
import com.solana.models.buffer.AccountInfoRule
import com.solana.models.buffer.MintRule
import com.solana.models.buffer.TokenSwapInfoRule
import com.solana.models.buffer.moshi.AccountInfoJsonAdapter
import com.solana.models.buffer.moshi.MintJsonAdapter
import com.solana.models.buffer.moshi.TokenSwapInfoJsonAdapter
import com.solana.networking.RPCEndpoint
import com.solana.networking.models.RpcRequest
import com.solana.networking.models.RpcResponse
import com.solana.networking.socket.models.SocketMethod
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
    fun accountNotification()
    fun programNotification()
    fun signatureNotification()
    fun logsNotification()
    fun unsubscribed(id: String)
    fun subscribed(socketId: Long, id: String)
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

    fun writeToSocket(request: RpcRequest): Result<String> {
        val rpcRequestJsonAdapter = moshi.adapter(RpcRequest::class.java)
        val json = rpcRequestJsonAdapter.toJson(request)
        if(socket?.send(json) != true){ return Result.failure(SolanaSocketError.couldNotWrite) }
        return Result.success(request.id)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        if(enableDebugLogs) { Log.d(TAG, "connected")}
        delegate?.connected()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {

        if(enableDebugLogs) { Log.d(TAG, "text: $text")}
        val responseJsonAdapter = moshi.adapter(Map::class.java)
        try {
            val methodString = responseJsonAdapter.fromJson(text)?.get("method")
            methodString?.let {
                when(it){
                    SocketMethod.accountNotification.string -> {
                        delegate?.accountNotification()
                    }
                    SocketMethod.signatureNotification.string -> {
                        delegate?.signatureNotification()
                    }
                    SocketMethod.logsNotification.string -> {
                        delegate?.logsNotification()
                    }
                    SocketMethod.programNotification.string -> {
                        delegate?.programNotification()
                    }
                    else -> { }
                }
            } ?: run {
                val subscriptionAdapter: JsonAdapter<RpcResponse<Int>> = moshi.adapter(
                    Types.newParameterizedType(
                        RpcResponse::class.java,
                        Int::class.javaObjectType
                    )
                )
                val subscription = subscriptionAdapter.fromJson(text)

                val unSubscriptionAdapter: JsonAdapter<RpcResponse<Boolean>> = moshi.adapter(
                    Types.newParameterizedType(
                        RpcResponse::class.java,
                        Boolean::class.javaObjectType
                    )
                )
                val unSubscription = unSubscriptionAdapter.fromJson(text)
            }

        } catch (error: java.lang.Exception){
            delegate?.error(error)
        }
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        if(enableDebugLogs) { Log.d(TAG, "bytes")}

    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        if(enableDebugLogs) { Log.d(TAG, "closing")}
        delegate?.disconnecting(code, reason)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        if(enableDebugLogs) { Log.d(TAG, "closed")}
        delegate?.disconnected(code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        if(enableDebugLogs) { Log.d(TAG, "failure")}
        delegate?.error(Exception(t))
    }
}