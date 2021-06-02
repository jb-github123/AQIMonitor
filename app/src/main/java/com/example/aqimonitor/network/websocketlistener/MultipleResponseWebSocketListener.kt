package com.example.aqimonitor.network.websocketlistener

import android.util.Log
import com.example.aqimonitor.config.DEBUG_ON
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class MultipleResponseWebSocketListener(
    private val onMultipleMessageReceivedListener: OnMultipleMessageReceivedListener
) : WebSocketListener() {

    interface OnMultipleMessageReceivedListener {
        fun onMultipleAQIDataReceived(jsonString: String)
        fun onConnectionFailure()
    }

    private val TAG = MultipleResponseWebSocketListener::class.java.simpleName

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        if (DEBUG_ON) Log.e(TAG, "WebSocket opened - $response")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        if (DEBUG_ON) Log.e(TAG, "WebSocket response - $text")
        onMultipleMessageReceivedListener.onMultipleAQIDataReceived(text)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        if (DEBUG_ON) {
            Log.e(TAG, "WebSocket Failure - $response")
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