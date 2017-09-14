package com.zhangyang.ringbell.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.zhangyang.ringbell.utils.dpToPx
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by zhangyang on 2017/9/14.
 */
class CalendarView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {

    constructor(context: Context?) : this(context, null, 0)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    var calendar: Calendar = Calendar.getInstance()

    var cellList: ArrayList<CalendarCell> = ArrayList()

    var rectPaint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        rectPaint.color = Color.RED
        rectPaint.style = Paint.Style.STROKE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(minimumWidth, minimumHeight)
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(minimumWidth, heightSize)
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, minimumHeight)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        initCells()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (cell in cellList){
            canvas?.drawRect(cell.rect,rectPaint)
        }
    }

    //初始化cell的值
    private fun initCells() {
//        dpToPx(2f,context)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val cellWidth = (width - paddingLeft - paddingRight) / 7f
        val cellHeight = (height - paddingTop - paddingBottom) / 5f
        for (j in 0..4) {
            for (i in 0..6) {
                var cell = CalendarCell()
                cell.rect = RectF(cellWidth * i, cellHeight * j, cellWidth * (i + 1), cellHeight * (j + 1))
                cellList.add(cell)
            }
        }
    }

    inner class CalendarCell {
        var rect: RectF? = null
        var isToday: Boolean = false


    }

}