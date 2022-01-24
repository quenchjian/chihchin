package me.quenchjian.chihchin.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import me.quenchjian.chihchin.R
import me.quenchjian.chihchin.webservice.Trade
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class TradeView(private val root: View) {

  private val recyclerView: RecyclerView = root.findViewById(R.id.recycler_trade)
  private val progressBar: ProgressBar = root.findViewById(R.id.progress_trade)
  private val adapter: Adapter get() = recyclerView.adapter as Adapter

  init {
    recyclerView.layoutManager = LinearLayoutManager(root.context)
    recyclerView.adapter = Adapter()
  }

  fun toggleLoading(active: Boolean) {
    progressBar.visibility = if (active) View.VISIBLE else View.GONE
  }

  fun showTrade(trade: List<Trade>) {
    adapter.submitList(trade)
  }

  fun showError(msg: String) {
    Snackbar.make(root, msg, Snackbar.LENGTH_SHORT).show()
  }

  private class Holder(view: View) : RecyclerView.ViewHolder(view) {

    private val timeTextView: TextView = view.findViewById(R.id.text_time)
    private val priceTextView: TextView = view.findViewById(R.id.text_price)
    private val quantityTextView: TextView = view.findViewById(R.id.text_quantity)
    private val dateFormatter: SimpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private val priceFormatter: DecimalFormat = DecimalFormat("###,###.00")
    private val quantityFormatter: DecimalFormat = DecimalFormat("0.000000")

    fun bind(trade: Trade) {
      timeTextView.text = dateFormatter.format(trade.time)
      priceTextView.text = priceFormatter.format(trade.price)
      quantityTextView.text = quantityFormatter.format(trade.quantity)
    }
  }

  private class DiffCallback : DiffUtil.ItemCallback<Trade>() {
    override fun areItemsTheSame(oldItem: Trade, newItem: Trade): Boolean {
      return oldItem.collectId == newItem.collectId
    }

    override fun areContentsTheSame(oldItem: Trade, newItem: Trade): Boolean {
      return oldItem == newItem
    }
  }

  private class Adapter : ListAdapter<Trade, Holder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
      val inflater = LayoutInflater.from(parent.context)
      return Holder(inflater.inflate(R.layout.item_trade, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
      holder.bind(getItem(position))
    }
  }
}