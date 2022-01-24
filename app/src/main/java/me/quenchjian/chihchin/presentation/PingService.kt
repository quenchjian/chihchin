package me.quenchjian.chihchin.presentation

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import androidx.core.content.ContextCompat
import me.quenchjian.chihchin.App
import java.util.concurrent.TimeUnit

class PingService : JobService() {

  override fun onStartJob(params: JobParameters?): Boolean {
    App.get(this).updateService.pong()
    return false
  }

  override fun onStopJob(params: JobParameters?): Boolean {
    return false
  }

  companion object {
    private const val JOB_ID = 1
    fun schedule(context: Context, period: Long, unit: TimeUnit) {
      val job = JobInfo.Builder(JOB_ID, ComponentName(context, PingService::class.java))
        .setPeriodic(unit.toMillis(period))
        .build()
      val jobScheduler = ContextCompat.getSystemService(context, JobScheduler::class.java)!!
      jobScheduler.schedule(job)
    }

    fun cancel(context: Context) {
      val jobScheduler = ContextCompat.getSystemService(context, JobScheduler::class.java)!!
      jobScheduler.cancel(JOB_ID)
    }
  }
}