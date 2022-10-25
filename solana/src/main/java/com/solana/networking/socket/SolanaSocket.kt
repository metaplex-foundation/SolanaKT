package com.solana.networking.socket

import com.solana.api.AccountInfo
import com.solana.api.ProgramAccountSerialized
import com.solana.models.buffer.*
import com.solana.networking.*
import com.solana.networking.serialization.serializers.base64.BorshAsBase64JsonArraySerializer
import com.solana.networking.socket.models.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*
import okhttp3.*
import okio.ByteString

sealed class SolanaSocketError : Exception() {
    object disconnected : SolanaSocketError()
    object couldNotSerialize : SolanaSocketError()
    object couldNotWrite : SolanaSocketError()
}

interface SolanaSocketEventsDelegate {
    fun connected()
    fun accountNotification(notification: SocketResponse<AccountInfo<AccountInfoData?>>)
    fun programNotification(notification: SocketResponse<ProgramAccountSerialized<AccountInfo<AccountInfoData?>>>)
    fun signatureNotification(notification: SocketResponse<SignatureNotification>)
    fun logsNotification(notification: SocketResponse<LogsNotification>)
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
) : WebSocketListener() {
    private val TAG = "SOLANA_SOCKET"
    private var socket: WebSocket? = null
    private var delegate: SolanaSocketEventsDelegate? = null

    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    fun start(delegate: SolanaSocketEventsDelegate) {
        this.delegate = delegate
        val request: Request = Request.Builder().url(endpoint.urlWebSocket).build()
        socket = client.newWebSocket(request, this)
        client.dispatcher.executorService.shutdown()
    }

    fun stop() {
        socket?.cancel()
    }

    fun accountSubscribe(publicKey: String): Result<String> {
        val params = buildJsonArray {
            add(publicKey)
            add(buildJsonObject {
                put("encoding", Encoding.base64.getEncoding())
                put("commitment", Commitment.RECENT.value)
            })
        }
        val rpcRequest =
            RpcRequest(method = SocketMethod.accountSubscribe.string, params)
        return writeToSocket(rpcRequest)
    }

    fun accountUnSubscribe(socketId: Int): Result<String> {
        val params = buildJsonArray {
            add(socketId)
        }
        val rpcRequest =
            RpcRequest(method = SocketMethod.accountUnsubscribe.string, params)
        return writeToSocket(rpcRequest)
    }

    fun logsSubscribe(mentions: List<String>): Result<String> {
        val params = buildJsonArray {
            add(buildJsonObject {
                put("mentions", buildJsonArray {
                    mentions.forEach {
                        add((it))
                    }
                })
            })
            add(buildJsonObject {
                put("encoding", Encoding.base64.getEncoding())
                put("commitment", Commitment.RECENT.value)
            })
        }
        val rpcRequest = RpcRequest(method = SocketMethod.logsSubscribe.string, params)
        return writeToSocket(rpcRequest)
    }

    fun logsSubscribeAll(): Result<String> {
        val params = buildJsonArray {
            add("all")
            add(buildJsonObject {
                put("encoding", Encoding.base64.getEncoding())
                put("commitment", Commitment.RECENT.value)
            })
        }
        val rpcRequest = RpcRequest(method = SocketMethod.logsSubscribe.string, params)
        return writeToSocket(rpcRequest)
    }

    fun logsUnsubscribe(socketId: Int): Result<String> {
        val params = buildJsonArray {
            add(socketId)
        }
        val rpcRequest =
            RpcRequest(method = SocketMethod.logsUnsubscribe.string, params)
        return writeToSocket(rpcRequest)
    }

    fun programSubscribe(publicKey: String): Result<String> {
        val params = buildJsonArray {
            add(publicKey)
            add(buildJsonObject {
                put("encoding", Encoding.base64.getEncoding())
                put("commitment", Commitment.RECENT.value)
            })
        }
        val rpcRequest =
            RpcRequest(method = SocketMethod.programSubscribe.string, params)
        return writeToSocket(rpcRequest)
    }

    fun programUnsubscribe(socketId: Int): Result<String> {
        val params = buildJsonArray {
            add(socketId)
        }
        val rpcRequest =
            RpcRequest(method = SocketMethod.programUnsubscribe.string, params)
        return writeToSocket(rpcRequest)
    }

    fun signatureSubscribe(signature: String): Result<String> {
        val params = buildJsonArray {
            add(signature)
            add(buildJsonObject {
                put("encoding", Encoding.base64.getEncoding())
                put("commitment", Commitment.RECENT.value)
            })
        }
        val rpcRequest =
            RpcRequest(method = SocketMethod.signatureSubscribe.string, params)
        return writeToSocket(rpcRequest)
    }

    fun signatureUnsubscribe(socketId: Int): Result<String> {
        val params = buildJsonArray {
            add(socketId)
        }
        val rpcRequest =
            RpcRequest(method = SocketMethod.signatureUnsubscribe.string, params)
        return writeToSocket(rpcRequest)
    }

    fun writeToSocket(request: RpcRequest): Result<String> {
        val json = json.encodeToString(RpcRequest.serializer(), request)
        if (socket?.send(json) != true) {
            return Result.failure(SolanaSocketError.couldNotWrite)
        }
        return Result.success(request.id)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        if (enableDebugLogs) {
            println(TAG + " connected")
        }
        delegate?.connected()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {

        if (enableDebugLogs) {
            println(TAG + " text: $text")
        }

        try {
            val dictJson = json.decodeFromString(RPCResponseMethond.serializer(), text)
            val methodString = dictJson.method
            methodString?.let {
                when (it) {
                    SocketMethod.accountNotification.string -> {
                        val serializer = SocketResponse.serializer(
                            AccountInfo.serializer(BorshAsBase64JsonArraySerializer(AccountInfoData.serializer()))
                        )
                        json.decodeFromString(serializer, text).let { response ->
                            delegate?.accountNotification(response)
                        }
                    }
                    SocketMethod.signatureNotification.string -> {
                        val serializer = SocketResponse.serializer(
                            (SignatureNotification.serializer())
                        )
                        json.decodeFromString(serializer, text).let { response ->
                            delegate?.signatureNotification(response)
                        }
                    }
                    SocketMethod.logsNotification.string -> {
                        val serializer = SocketResponse.serializer(
                            (LogsNotification.serializer())
                        )
                        json.decodeFromString(serializer, text).let { response ->
                            delegate?.logsNotification(response)
                        }
                    }
                    SocketMethod.programNotification.string -> {
                        val serializer = SocketResponse.serializer(
                            ProgramAccountSerialized.serializer(
                                AccountInfo.serializer(
                                    BorshAsBase64JsonArraySerializer(AccountInfoData.serializer().nullable)
                                )
                            )
                        )

                        json.decodeFromString(serializer, text).let { response ->
                            delegate?.programNotification(response)
                        }
                    }
                    else -> {}
                }
            } ?: run {
                if (dictJson.result?.intOrNull is Int) {
                    val serializer = RpcResponse.serializer(Int.serializer())

                    json.decodeFromString(serializer, text).let { response ->
                        response.result?.let { result ->
                            response.id?.let { id ->
                                delegate?.subscribed(
                                    result,
                                    id
                                )
                            }
                        }
                    }
                }

                if (dictJson.result?.booleanOrNull is Boolean) {
                    val serializer = RpcResponse.serializer(Boolean.serializer())
                    json.decodeFromString(serializer, text).let { response ->
                        response.id?.let { delegate?.unsubscribed(it) }
                    }
                }
            }
        } catch (error: java.lang.Exception) {
            delegate?.error(error)
        }
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        if (enableDebugLogs) {
            println(TAG + " bytes")
        }

    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        if (enableDebugLogs) {
            println(TAG + " closing")
        }
        delegate?.disconnecting(code, reason)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        if (enableDebugLogs) {
            println(TAG + " closed")
        }
        delegate?.disconnected(code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        if (enableDebugLogs) {
            println(TAG + " failure")
        }
        delegate?.error(Exception(t))
    }
}

@Serializable
data class RPCResponseMethond(val method: String? = null, val result: JsonPrimitive? = null)