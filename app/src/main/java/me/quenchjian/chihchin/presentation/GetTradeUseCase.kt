package me.quenchjian.chihchin.presentation

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.quenchjian.chihchin.webservice.Api
import me.quenchjian.chihchin.webservice.Trade
import kotlin.coroutines.CoroutineContext

class GetTradeUseCase(private val api: Api): CoroutineScope {

  override val coroutineContext: CoroutineContext = Dispatchers.Main

  interface Result {
    fun onSuccess(trades: List<Trade>)
    fun onFailure(error: Throwable)
  }

  private val listeners = mutableSetOf<Result>()

  fun register(result: Result) {
    listeners.add(result)
  }

  fun unregister(result: Result) {
    listeners.remove(result)
  }

  fun dispose() {
    listeners.clear()
  }

  operator fun invoke(symbol: String = "BTCUSDT") {
    Log.d("GetTradeUseCase", "start")
    launch {
      withContext(Dispatchers.IO) { api.getTrades(symbol) }
        .onSuccess { trade ->
          Log.d("GetTradeUseCase", "success")
          listeners.forEach { it.onSuccess(trade) }
        }
        .onFailure { e ->
          Log.d("GetTradeUseCase", "failure ${e.message}")
          listeners.forEach { it.onFailure(e) }
        }
    }
    Log.d("GetTradeUseCase", "end")
  }
}