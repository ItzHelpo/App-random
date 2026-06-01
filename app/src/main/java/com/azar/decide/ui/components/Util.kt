package com.azar.decide.ui.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View

/** Walks up the context chain to find the hosting Activity. */
fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

/** Lightweight haptic tick used when a result is revealed. */
fun View.performResultHaptic() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        performHapticFeedback(HapticFeedbackConstants.CONFIRM)
    } else {
        @Suppress("DEPRECATION")
        performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
    }
}
