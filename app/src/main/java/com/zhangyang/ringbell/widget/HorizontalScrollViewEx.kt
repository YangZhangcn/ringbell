package com.zhangyang.ringbell.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout

/**
 * Created by zhangyang on 2017/9/28.
 * 类似viewpager可以横向滑动的viewGroup
 */
class HorizontalScrollViewEx(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : ViewGroup(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context,null,0)

    constructor(context: Context,attrs: AttributeSet?):this(context,attrs,0)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var measuredWidth = 0
        var measuredHeight = 0
        measureChildren(widthMeasureSpec,heightMeasureSpec)

        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)

        if (childCount == 0){
            if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
                setMeasuredDimension(minimumWidth,minimumHeight)
            }else if(widthSpecMode == MeasureSpec.AT_MOST){
                setMeasuredDimension(minimumWidth,heightSpecSize)
            }else if (heightSpecMode == MeasureSpec.AT_MOST){
                setMeasuredDimension(widthSpecSize,minimumHeight)
            }
        }else{
            if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
                for(i in 0 until childCount){
                    val layoutParams = getChildAt(i).layoutParams as LinearLayout.LayoutParams
                    measuredWidth += (getChildAt(i).measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin)
                }
            }

        }

    }

    override fun onLayout(b: Boolean, i: Int, i1: Int, i2: Int, i3: Int) {

    }
}
