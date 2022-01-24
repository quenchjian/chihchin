package me.quenchjian.chihchin.webservice

interface Api {
  suspend fun getTrades(symbol: String, limit: Int = 500): Result<List<Trade>>
}