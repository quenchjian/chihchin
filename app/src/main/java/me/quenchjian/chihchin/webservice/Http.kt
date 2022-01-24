package me.quenchjian.chihchin.webservice

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class Http(private val okHttpClient: OkHttpClient) : Api {

  companion object {
    private const val DOMAIN = "https://api.yshyqxx.com"
  }

  @Suppress("BlockingMethodInNonBlockingContext")
  override suspend fun getTrades(symbol: String, limit: Int): Result<List<Trade>> {
    return try {
      check(limit in 0..1000) { "limit($limit) large than 1000 or less than 0" }
      val l = if (limit == 0) 500 else limit
      val req = Request.Builder()
        .url("$DOMAIN/api/v1/aggTrades?symbol=$symbol&limit=$l")
        .get()
        .build()
      val resp = okHttpClient.newCall(req).execute()
      val body = resp.body?.string() ?: throw IOException("ResponseBody is Null")
      if (!resp.isSuccessful) {
        throw HttpError.parse(body)
      }
      Result.success(Trade.parse(body))
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
}