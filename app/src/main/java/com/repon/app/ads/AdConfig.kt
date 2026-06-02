package com.repon.app.ads

import com.repon.app.BuildConfig

/**
 * AdMob identifiers. Ships with Google's official TEST banner unit so test ads
 * appear out of the box. Replace [PROD_BANNER] with your own AdMob banner unit
 * (and the APPLICATION_ID in AndroidManifest.xml) before publishing.
 *
 * Debug builds always use the test unit, so you never accidentally click your
 * own real ads (which can get your AdMob account banned).
 */
object AdConfig {
    private const val TEST_BANNER = "ca-app-pub-3940256099942544/6300978111"

    // 👉 Replace with your own banner ad unit ID before publishing.
    private const val PROD_BANNER = "ca-app-pub-3940256099942544/6300978111"

    val bannerUnitId: String
        get() = if (BuildConfig.DEBUG) TEST_BANNER else PROD_BANNER
}
