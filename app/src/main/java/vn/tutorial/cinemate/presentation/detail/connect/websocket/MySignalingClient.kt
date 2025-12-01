package vn.tutorial.cinemate.presentation.detail.connect.websocket

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import okio.ByteString
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

//class MySignalingClient(private var signalingUrl: String) {
//
//    private var ws: WebSocket? = null
//    private var isConnected = false
//    private val client = OkHttpClient()
//
//    suspend fun connect(url: String? = null) {
//        if (url != null) {
//            signalingUrl = url
//        }
//
//        suspendCancellableCoroutine<Unit> { cont ->
//            try {
//                val request = Request.Builder()
//                    .url(signalingUrl)
//                    .build()
//
//                ws = client.newWebSocket(request, object : WebSocketListener() {
//
//                    override fun onOpen(webSocket: WebSocket, response: Response) {
//                        println("[SignalingClient] Connected to signaling server")
//                        isConnected = true
//                        startHeartbeat()
//                        cont.resume(Unit)
//                        // emit "connected" event if you have a listener system
//                    }
//
//                    override fun onMessage(webSocket: WebSocket, text: String) {
//                        handleMessage(text)
//                    }
//
//                    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
//                        // nếu bạn muốn handle binary message
//                    }
//
//                    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
//                        println("[SignalingClient] WebSocket error: ${t.message}")
//                        cont.resumeWithException(t)
//                        // emit "error" event nếu có listener
//                    }
//
//                    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
//                        println("[SignalingClient] WebSocket closing: $reason")
//                        isConnected = false
//                        stopHeartbeat()
//                        scheduleReconnect()
//                        webSocket.close(code, reason)
//                        // emit "disconnected" event nếu có listener
//                    }
//
//                    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
//                        println("[SignalingClient] WebSocket closed: $reason")
//                        isConnected = false
//                        stopHeartbeat()
//                        scheduleReconnect()
//                    }
//                })
//
//                // Connection timeout
//                val timeoutMs = 5000L
//                cont.invokeOnCancellation {
//                    if (!isConnected) {
//                        ws?.cancel()
//                        cont.resumeWithException(Exception("Signaling connection timeout"))
//                    }
//                }
//
//            } catch (e: Exception) {
//                cont.resumeWithException(e)
//            }
//        }
//    }
//
//    private fun startHeartbeat() {
//        // implement heartbeat logic
//    }
//
//    private fun stopHeartbeat() {
//        // stop heartbeat
//    }
//
//    private fun scheduleReconnect() {
//        // implement reconnection logic
//    }
//
//    private fun handleMessage(message: String) {
//        // implement message handling
//    }
//}
