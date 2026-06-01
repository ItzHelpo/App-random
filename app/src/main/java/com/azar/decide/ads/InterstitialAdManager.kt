package com.azar.decide.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

/**
 * Loads and shows interstitial ads, but only rarely. An interstitial appears at
 * most once every [AdConfig.INTERSTITIAL_MIN_INTERVAL_MS] AND after the user has
 * performed at least [AdConfig.INTERSTITIAL_ACTION_THRESHOLD] actions. The goal
 * is monetization that respects the user, so the app never feels spammy.
 */
object InterstitialAdManager {

    private const val TAG = "InterstitialAd"

    private var interstitial: InterstitialAd? = null
    private var isLoading = false

    private var actionCount = 0
    private var lastShownAt = 0L

    /** Preload an interstitial so it is ready when we decide to show one. */
    fun preload(context: Context) {
        if (interstitial != null || isLoading) return
        isLoading = true
        InterstitialAd.load(
            context.applicationContext,
            AdConfig.interstitialUnitId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitial = ad
                    isLoading = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.d(TAG, "Interstitial failed to load: ${error.message}")
                    interstitial = null
                    isLoading = false
                }
            }
        )
    }

    /** Call this every time the user generates/flips/rolls something. */
    fun recordAction() {
        actionCount++
    }

    /**
     * Shows an interstitial if the frequency caps allow it. Returns true if an ad
     * was shown. Always preloads the next ad afterwards.
     */
    fun maybeShow(activity: Activity): Boolean {
        val now = System.currentTimeMillis()
        val enoughActions = actionCount >= AdConfig.INTERSTITIAL_ACTION_THRESHOLD
        val enoughTime = now - lastShownAt >= AdConfig.INTERSTITIAL_MIN_INTERVAL_MS
        val ad = interstitial

        if (ad == null || !enoughActions || !enoughTime) {
            if (ad == null) preload(activity)
            return false
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitial = null
                preload(activity)
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                interstitial = null
                preload(activity)
            }
        }

        ad.show(activity)
        lastShownAt = now
        actionCount = 0
        return true
    }
}
