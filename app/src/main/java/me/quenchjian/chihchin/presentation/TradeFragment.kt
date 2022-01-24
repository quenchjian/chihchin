package me.quenchjian.chihchin.presentation

import android.app.job.JobScheduler
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import me.quenchjian.chihchin.App
import me.quenchjian.chihchin.R
import me.quenchjian.chihchin.webservice.Trade
import me.quenchjian.chihchin.webservice.UpdateService
import java.util.concurrent.TimeUnit

class TradeFragment : Fragment(R.layout.view_trade), CoroutineScope by MainScope() {

  private lateinit var tradeView: TradeView
  private lateinit var getTrade: GetTradeUseCase
  private lateinit var updateService: UpdateService
  private lateinit var jobScheduler: JobScheduler
  private val trade = mutableListOf<Trade>()

  override fun onAttach(context: Context) {
    super.onAttach(context)
    getTrade = GetTradeUseCase(App.get(context).api)
    updateService = App.get(context).updateService
    jobScheduler = ContextCompat.getSystemService(context, JobScheduler::class.java)!!
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    tradeView = TradeView(view)
  }

  override fun onStart() {
    super.onStart()
    getTrade.register(object : GetTradeUseCase.Result {
      override fun onSuccess(trades: List<Trade>) {
        trade.addAll(trades)
        sortAndShow()
        scheduleUpdate()
      }

      override fun onFailure(error: Throwable) {
        tradeView.showError(error.message ?: "")
      }
    })
    updateService.register { event ->
      val t = trade.find { it.collectId == event.collectId }
      if (t != null) {
        trade[trade.indexOf(t)] = event.trade
      } else {
        trade.add(event.trade)
      }
      launch {
        sortAndShow()
      }
    }
    tradeView.toggleLoading(true)
    getTrade()
    tradeView.toggleLoading(false)
  }

  override fun onStop() {
    super.onStop()
    getTrade.dispose()
  }

  override fun onDestroy() {
    super.onDestroy()
    updateService.disconnect()
    PingService.cancel(requireContext())
  }

  private fun sortAndShow() {
    val sorted = trade.sortedByDescending { it.time }
    tradeView.showTrade(sorted.subList(0, 40))
  }

  private fun scheduleUpdate() {
    updateService.connect("btcusdt@aggTrade") {
      PingService.schedule(requireContext(), 10, TimeUnit.MINUTES)
    }
  }

  companion object {
    const val TAG = "TradeFragment"
  }
}