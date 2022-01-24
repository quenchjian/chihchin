package me.quenchjian.chihchin.webservice

interface UpdateService {
  fun connect(streamName: String, onConnected: () -> Unit = {})
  fun disconnect()
  fun pong()
  fun register(event: OnReceiveEvent)
  fun unregister(event: OnReceiveEvent)

  fun interface OnReceiveEvent {
    fun onReceive(event: Event)
  }
}