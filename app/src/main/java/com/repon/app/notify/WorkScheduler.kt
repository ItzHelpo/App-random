package com.repon.app.notify

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object WorkScheduler {

    private const val DAILY_WORK = "restock_check_daily"
    private const val ONESHOT_WORK = "restock_check_now"

    /** Schedules the once-a-day low-stock check (idempotent). */
    fun scheduleDaily(context: Context) {
        val request = PeriodicWorkRequestBuilder<RestockCheckWorker>(1, TimeUnit.DAYS).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            DAILY_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    /** Runs the check immediately (e.g. after the user adds or edits an item). */
    fun triggerNow(context: Context) {
        val request = OneTimeWorkRequestBuilder<RestockCheckWorker>().build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            ONESHOT_WORK,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}
