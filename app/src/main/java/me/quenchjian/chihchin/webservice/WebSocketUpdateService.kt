package me.quenchjian.chihchin.webservice

import android.util.Log
import okhttp3.*

class WebSocketUpdateService(private val okHttpClient: OkHttpClient) : WebSocketListener(),
  UpdateService {

  companion object {
    private const val TAG = "WebSocket"
    private const val DOMAIN = "wss://stream.yshyqxx.com"
  }

  private var socket: WebSocket? = null
  private var onOpen: () -> Unit = {}
  private val listeners = mutableSetOf<UpdateService.OnReceiveEvent>()

  override fun connect(streamName: String, onConnected: () -> Unit) {
    onOpen = onConnected
    val req = Request.Builder().url("$DOMAIN/ws/$streamName").build()
    okHttpClient.newWebSocket(req, this)
  }

  override fun disconnect() {
    listeners.clear()
    socket?.close(1000, null)
  }

  override fun pong() {
    socket?.send("pong")
  }

  override fun register(event: UpdateService.OnReceiveEvent) {
    listeners.add(event)
  }

  override fun unregister(event: UpdateService.OnReceiveEvent) {
    listeners.remove(event)
  }

  override fun onOpen(webSocket: WebSocket, response: Response) {
    Log.d(TAG, "connected")
    socket = webSocket
    onOpen.invoke()
  }

  override fun onMessage(webSocket: WebSocket, text: String) {
    val event = Event.parse(text)
    listeners.forEach { it.onReceive(event) }
  }

  override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
    Log.d(TAG, "disconnected")
    socket = null
  }
}