package com.azar.decide.ads

import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

/**
 * An adaptive anchored banner. It sits quietly at the bottom of the screen and
 * automatically sizes itself to the device width — the least intrusive ad
 * format Google offers.
 */
@Composable
fun BannerAd(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val adWidth = configuration.screenWidthDp

    // Remember the AdView so it survives recomposition instead of reloading.
    val adView = remember {
        AdView(context).apply {
            adUnitId = AdConfig.bannerUnitId
            setAdSize(
                AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
            )
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            loadAd(AdRequest.Builder().build())
        }
    }

    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { adView }
    )
}
