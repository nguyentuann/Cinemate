package vn.tutorial.cinemate.presentation.detail.connect.websocket

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import vn.tutorial.cinemate.core.util.LogUtil
import java.net.URI

class SignalingClient(
    private val serverUrl: String,
    private val onMessage: (String) -> Unit
) {
    private val wsClient: WebSocketClient = object : WebSocketClient(URI(serverUrl)) {
        override fun onOpen(handshakedata: ServerHandshake?) {
            LogUtil("Connected to server")
        }
        override fun onMessage(message: String?) {
            message?.let { onMessage(it) }
        }
        override fun onClose(code: Int, reason: String?, remote: Boolean) {
            LogUtil("Disconnected: $reason")
        }
        override fun onError(ex: Exception?) {
            LogUtil("Error + $ex")
        }
    }

    init {
        wsClient.connect()
    }

    fun send(message: String) {
        wsClient.send(message)
    }
}
