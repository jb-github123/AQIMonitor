package com.example.aqimonitor.network.websocketlistener

import android.util.Log
import com.example.aqimonitor.config.DEBUG_ON
import com.example.aqimonitor.network.NetworkManager
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class SingleResponseWebSocketListener(
    private val onSingleMessageReceivedListener: OnSingleMessageReceivedListener
) : WebSocketListener() {

    interface OnSingleMessageReceivedListener {
        fun onSingleAQIDataReceived(jsonString: String)
    }

    private val TAG = SingleResponseWebSocketListener::class.java.simpleName

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        Log.e(TAG, "WebSocket opened - $response")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.e(TAG, "WebSocket response - $text")
        webSocket.close(
            NetworkManager.SOCKET_NORMAL_CLOSE,
            "Data pulled for storing in db. Thank You!"
        )

        onSingleMessageReceivedListener.onSingleAQIDataReceived(text)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        if (DEBUG_ON) {
            if (DEBUG_ON) Log.e(TAG, "WebSocket Failure - $response")
            t.printStackTrace()
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        if (DEBUG_ON) Log.e(TAG, "WebSocket Closing - $code - $reason")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        if (DEBUG_ON) Log.e(TAG, "WebSocket Closed - $code - $reason")
    }

}