package me.quenchjian.chihchin.webservice

import org.json.JSONObject
import java.io.IOException

class HttpError(val code: Int, val msg: String) : IOException("HttpError: $msg($code)") {

  companion object {
    fun parse(json: String): HttpError {
      val obj = JSONObject(json)
      return HttpError(obj.getInt("code"), obj.getString("msg"))
    }
  }
}