package me.quenchjian.chihchin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.quenchjian.chihchin.presentation.TradeFragment

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    supportFragmentManager.beginTransaction()
      .replace(R.id.container, TradeFragment(), TradeFragment.TAG)
      .commit()
  }
}