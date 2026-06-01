package com.azar.decide

import android.app.Application
import com.azar.decide.ads.InterstitialAdManager
import com.google.android.gms.ads.MobileAds
import kotlin.concurrent.thread

class AzarApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize the Mobile Ads SDK off the main thread (recommended by Google).
        thread {
            MobileAds.initialize(this) {
                // Preload the first interstitial once initialization completes.
                InterstitialAdManager.preload(this)
            }
        }
    }
}
