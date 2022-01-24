package me.quenchjian.chihchin.webservice

import org.json.JSONObject
import java.math.BigDecimal
import java.util.*

data class Event(
  val type: String = "",
  val eventTime: Date = Date(),
  val pair: String = "",
  val collectId: Int = 0,
  val price: BigDecimal = BigDecimal.ZERO,
  val quantity: BigDecimal = BigDecimal.ZERO,
  val firstId: Int = 0,
  val lastId: Int = 0,
  val time: Date = Date(),
  val manualSell: Boolean = false,
  val autoBest: Boolean = false
) {
  val trade = Trade(collectId, price, quantity, firstId, lastId, time, manualSell, autoBest)

  companion object {
    fun parse(json: String): Event {
      val obj = JSONObject(json)
      return Event(
        obj.getString("e"),
        Date(obj.getLong("E")),
        obj.getString("s"),
        obj.getInt("a"),
        BigDecimal(obj.getString("p")),
        BigDecimal(obj.getDouble("q")),
        obj.getInt("f"),
        obj.getInt("l"),
        Date(obj.getLong("T")),
        obj.getBoolean("m"),
        obj.getBoolean("M"),
      )
    }
  }
}