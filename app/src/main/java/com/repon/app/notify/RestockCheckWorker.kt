package com.repon.app.notify

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.repon.app.data.AppDatabase
import com.repon.app.data.RestockCalculator
import com.repon.app.data.RestockStatus
import com.repon.app.data.SettingsStore
import java.time.LocalDate

/** Runs daily: warns about items that are running low, once per cycle. */
class RestockCheckWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val context = applicationContext

        if (!SettingsStore(context).remindersEnabledNow()) return Result.success()

        Notifications.ensureChannel(context)

        val dao = AppDatabase.get(context).itemDao()
        val today = LocalDate.now().toEpochDay()

        dao.getAllOnce().forEach { item ->
            if (!item.notify) return@forEach
            val status = RestockCalculator.statusFor(item, today).status
            val needsWarning = status == RestockStatus.SOON || status == RestockStatus.OUT
            val alreadyWarned = item.lastNotifiedCycleStart == item.startEpochDay
            if (needsWarning && !alreadyWarned) {
                Notifications.notifyRestock(context, item, status)
                dao.update(item.copy(lastNotifiedCycleStart = item.startEpochDay))
            }
        }

        return Result.success()
    }
}
