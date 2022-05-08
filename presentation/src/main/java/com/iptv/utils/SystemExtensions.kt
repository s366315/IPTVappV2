package com.iptv.utils

import android.view.View
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

fun Window.hideSystemUI(targetView: View) {
    WindowCompat.setDecorFitsSystemWindows(this, false)
    WindowInsetsControllerCompat(this, targetView).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun Window.showSystemUI(targetView: View) {
    WindowCompat.setDecorFitsSystemWindows(this, true)
    WindowInsetsControllerCompat(this, targetView).show(WindowInsetsCompat.Type.systemBars())
}