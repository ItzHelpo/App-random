package com.repon.app

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.repon.app.notify.Notifications
import com.repon.app.notify.WorkScheduler
import kotlin.concurrent.thread

class ReponApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Notifications.ensureChannel(this)
        WorkScheduler.scheduleDaily(this)
        // Initialize the ads SDK off the main thread (Google's recommendation).
        thread { MobileAds.initialize(this) {} }
    }
}
