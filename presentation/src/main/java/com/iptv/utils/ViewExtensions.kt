package com.iptv.utils

import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner
import kotlin.math.roundToInt


fun GradientDrawable.setTopCorners(radius: Int) {

}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

val Float.dp: Float
    get() = (this * Resources.getSystem().displayMetrics.density)

fun AppCompatSpinner.setOnItemChooseListener(position: (Int) -> Unit) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            position(position)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }
    }
}