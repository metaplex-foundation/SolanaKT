package com.solana.socket

import com.solana.networking.models.RpcNotificationResult
import com.solana.networking.models.RpcRequest
import com.solana.networking.models.RpcResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.net.URISyntaxException
import java.util.*


class SubscriptionWebSocketClient(serverURI: URI?) : WebSocketClient(serverURI) {
    private inner class SubscriptionParams constructor(
        request: RpcRequest,
        listener: NotificationEventListener
    ) {
        var request: RpcRequest
        var listener: NotificationEventListener

        init {
            this.request = request
            this.listener = listener
        }
    }

    private val subscriptions: MutableMap<String, SubscriptionParams> = HashMap()
    private val subscriptionIds: MutableMap<String, Long?> = HashMap()
    private val subscriptionLinsteners: MutableMap<Long, NotificationEventListener> = HashMap()
    fun accountSubscribe(key: String, listener: NotificationEventListener) {
        val params: MutableList<Any> = ArrayList()
        params.add(key)
        val rpcRequest = RpcRequest("accountSubscribe", params)
        subscriptions[rpcRequest.id] =
            SubscriptionParams(rpcRequest, listener)
        subscriptionIds[rpcRequest.id] = null
        updateSubscriptions()
    }

    fun signatureSubscribe(signature: String, listener: NotificationEventListener) {
        val params: MutableList<Any> = ArrayList()
        params.add(signature)
        val rpcRequest = RpcRequest("signatureSubscribe", params)
        subscriptions[rpcRequest.id] =
            SubscriptionParams(rpcRequest, listener)
        subscriptionIds[rpcRequest.id] = null
        updateSubscriptions()
    }

    override fun onOpen(handshakedata: ServerHandshake?) {
        updateSubscriptions()
    }

    override fun onMessage(message: String) {
        val resultAdapter: JsonAdapter<RpcResponse<Long>> = Moshi.Builder().build()
            .adapter(Types.newParameterizedType(RpcResponse::class.java, Long::class.java))
        try {
            val rpcResult: RpcResponse<Long> = resultAdapter.fromJson(message)!!
            val rpcResultId: String? = rpcResult.id
            if (rpcResultId != null) {
                if (subscriptionIds.containsKey(rpcResultId)) {
                    subscriptionIds[rpcResultId] = rpcResult.result
                    subscriptionLinsteners[rpcResult.result!!] =
                        subscriptions[rpcResultId]!!.listener
                    subscriptions.remove(rpcResultId)
                }
            } else {
                val notificationResultAdapter = Moshi.Builder().build()
                    .adapter(RpcNotificationResult::class.java)
                val result = notificationResultAdapter.fromJson(message)
                val listener = subscriptionLinsteners[result!!.params!!.subscription]
                val value = result.params!!.result!!.value as Map<*, *>
                when (result.method) {
                    "signatureNotification" -> listener!!.onNotificationEvent(
                        SignatureNotification(
                            value["err"]
                        )
                    )
                    "accountNotification" -> listener!!.onNotificationEvent(value)
                }
            }
        } catch (ex: Exception) {
            println(ex)
        }
    }

    override fun onClose(code: Int, reason: String, remote: Boolean) {
        println(
            "Connection closed by " + (if (remote) "remote peer" else "us") + " Code: " + code + " Reason: " + reason
        )
    }

    override fun onError(ex: Exception) {
        ex.printStackTrace()
    }

    private fun updateSubscriptions() {
        if (isOpen() && subscriptions.size > 0) {
            val rpcRequestJsonAdapter: JsonAdapter<RpcRequest> =
                Moshi.Builder().build().adapter<RpcRequest>(
                    RpcRequest::class.java
                )
            for (sub in subscriptions.values) {
                send(rpcRequestJsonAdapter.toJson(sub.request))
            }
        }
    }

    companion object {
        private var instance: SubscriptionWebSocketClient? = null
        fun getInstance(endpoint: String?): SubscriptionWebSocketClient? {
            val endpointURI: URI
            val serverURI: URI
            try {
                endpointURI = URI(endpoint)
                serverURI =
                    URI(if (endpointURI.getScheme() === "https") "wss" else "ws" + "://" + endpointURI.getHost())
            } catch (e: URISyntaxException) {
                throw IllegalArgumentException(e)
            }
            if (instance == null) {
                instance = SubscriptionWebSocketClient(serverURI)
            }
            if (instance?.isOpen() == true) {
                instance?.connect()
            }
            return instance
        }
    }
}
