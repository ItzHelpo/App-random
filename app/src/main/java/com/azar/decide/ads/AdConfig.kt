package com.azar.decide.ads

import com.azar.decide.BuildConfig

/**
 * Central place for every AdMob identifier.
 *
 * Out of the box this uses Google's official TEST ad units, so you can run the
 * app immediately and see (test) ads without an AdMob account. Real ads only
 * show after you:
 *   1. Create a free AdMob account: https://admob.google.com
 *   2. Register this app and create a Banner + Interstitial ad unit.
 *   3. Replace [PROD_BANNER] and [PROD_INTERSTITIAL] below with your IDs.
 *   4. Replace the APPLICATION_ID meta-data in AndroidManifest.xml.
 *
 * Test ad units are always used in debug builds. NEVER click your own real ads
 * during testing — Google may ban your account. That is why debug builds always
 * stay on the test units.
 */
object AdConfig {

    // Google's public TEST ad units — safe to click, never generate revenue.
    private const val TEST_BANNER = "ca-app-pub-3940256099942544/6300978111"
    private const val TEST_INTERSTITIAL = "ca-app-pub-3940256099942544/1033173712"

    // 👉 REPLACE these two with your own AdMob ad unit IDs before publishing.
    private const val PROD_BANNER = "ca-app-pub-3940256099942544/6300978111"
    private const val PROD_INTERSTITIAL = "ca-app-pub-3940256099942544/1033173712"

    val bannerUnitId: String
        get() = if (BuildConfig.DEBUG) TEST_BANNER else PROD_BANNER

    val interstitialUnitId: String
        get() = if (BuildConfig.DEBUG) TEST_INTERSTITIAL else PROD_INTERSTITIAL

    // --- Frequency capping (keeps ads non-intrusive) ---

    /** Minimum number of generate/flip/roll actions between two interstitials. */
    const val INTERSTITIAL_ACTION_THRESHOLD = 12

    /** Minimum time between two interstitials, in milliseconds (3 minutes). */
    const val INTERSTITIAL_MIN_INTERVAL_MS = 3 * 60 * 1000L
}
