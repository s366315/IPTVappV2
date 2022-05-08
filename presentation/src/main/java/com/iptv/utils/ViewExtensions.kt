package com.iptv.utils

import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlin.math.roundToInt


fun GradientDrawable.setTopCorners(radius: Int) {

}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

val Float.dp: Float
    get() = (this * Resources.getSystem().displayMetrics.density)