package me.quenchjian.chihchin

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import android.content.ContextWrapper
import me.quenchjian.chihchin.webservice.Api
import me.quenchjian.chihchin.webservice.Http
import me.quenchjian.chihchin.webservice.UpdateService
import me.quenchjian.chihchin.webservice.WebSocketUpdateService
import okhttp3.OkHttpClient

class App : Application() {

  lateinit var api: Api
  lateinit var updateService: UpdateService

  override fun onCreate() {
    super.onCreate()
    api = Http(OkHttpClient())
    updateService = WebSocketUpdateService(OkHttpClient())
  }

  companion object {
    fun get(context: Context): App {
      return when (context) {
        is Activity -> context.application as App
        is Service -> context.application as App
        is ContextWrapper -> get(context.baseContext)
        else -> throw IllegalArgumentException("Unknown context: $context")
      }
    }
  }
}