package me.quenchjian.chihchin.webservice

import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*

data class Trade(
  val collectId: Int = 0,
  val price: BigDecimal = BigDecimal.ZERO,
  val quantity: BigDecimal = BigDecimal.ZERO,
  val firstId: Int = 0,
  val lastId: Int = 0,
  val time: Date = Date(),
  val manualSell: Boolean = false,
  val autoBest: Boolean = false
) {

  companion object {
    fun parse(json: String): List<Trade> {
      val array = try {
        JSONArray(json)
      } catch (e: Exception) {
        JSONObject(json)
      }
      val list = mutableListOf<Trade>()
      try {
        when (array) {
          is JSONObject -> list.add(parseTrade(array))
          is JSONArray -> {
            for (i in 0 until array.length()) {
              list.add(parseTrade(array.getJSONObject(i)))
            }
          }
        }
      } catch (e: Exception) {
      }
      return list
    }

    private fun parseTrade(json: JSONObject): Trade {
      return Trade(
        json.getInt("a"),
        BigDecimal(json.getString("p")),
        BigDecimal(json.getDouble("q")),
        json.getInt("f"),
        json.getInt("l"),
        Date(json.getLong("T")),
        json.getBoolean("m"),
        json.getBoolean("M"),
      )
    }
  }
}