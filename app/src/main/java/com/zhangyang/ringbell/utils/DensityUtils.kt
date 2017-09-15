package com.zhangyang.ringbell.utils

import android.content.Context
import android.util.TypedValue

/**
 * dp转px
 */
fun dpToPx(dpVal: Float, context: Context): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        dpVal, context.resources.displayMetrics)

/**
 * px转dp
 */
fun pxToDp(pxVal: Float, context: Context): Float = pxVal / context.resources.displayMetrics.density

fun spToPx(spVal:Float,context: Context):Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
        spVal, context.resources.displayMetrics)