package com.zhangyang.ringbell.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import com.zhangyang.ringbell.utils.dpToPx
import com.zhangyang.ringbell.utils.spToPx
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by zhangyang on 2017/9/14.
 */
class CalendarView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {

    constructor(context: Context?) : this(context, null, 0)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    var calendar: Calendar = Calendar.getInstance()

    private var cellList: ArrayList<CalendarCell> = ArrayList()

    private var rectPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var circlePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    //行数
    private val lineNum = 7
    //列数
    private val columnNum = 7

    private val currentDate = arrayOf(0, 0, 0)

    private val TAG = "CalendarView"

    private var textHeight = 0

    private var pointToday: PointF = PointF()

    private var todayCircleRadius: Float = 0f

    private var maxCircleRadius: Float = 0f

    private var animator: ValueAnimator? = null

    private val week = arrayOf("日", "一", "二", "三", "四", "五", "六")

    private var titleHeight : Int = 0

    init {
        rectPaint.color = Color.RED
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = dpToPx(0.1f, context!!)

        textPaint.textSize = spToPx(16f, context)
        textPaint.color = Color.GRAY
        textPaint.textAlign = Paint.Align.CENTER

        circlePaint.color = Color.RED
        circlePaint.style = Paint.Style.FILL

        var rect = Rect()
        textPaint.getTextBounds("123", 0, 1, rect)
        textHeight = rect.bottom - rect.top
        currentDate[0] = calendar.get(Calendar.YEAR)
        currentDate[1] = calendar.get(Calendar.MONTH) + 1
        currentDate[2] = calendar.get(Calendar.DAY_OF_MONTH)
        Log.d(TAG, "" + currentDate[0] + currentDate[1] + currentDate[2])
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator?.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.end()
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
        initCells(changed)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawCircle(pointToday.x, pointToday.y, todayCircleRadius, circlePaint)
        canvas?.drawText(currentDate[0].toString() + "年" + currentDate[1] + "月",width/2f,titleHeight- dpToPx(2f,context),textPaint)
        for (i in 0 until cellList.size) {
            var cell = cellList[i]
            if (cell.isToday) {
                textPaint.color = Color.WHITE
            } else {
                textPaint.color = Color.GRAY
            }
            if (cell.isChecked){
                canvas?.drawCircle((cell.rect!!.left + cell.rect!!.right) / 2,(cell.rect!!.bottom + cell.rect!!.top) / 2,maxCircleRadius,rectPaint)
            }
//            canvas?.drawRect(cell.rect, rectPaint)
            if (cell.date[0] != 0) {
                canvas?.drawText( cell.date[2].toString(),
                        (cell.rect!!.left + cell.rect!!.right) / 2,
                        (cell.rect!!.bottom + cell.rect!!.top) / 2 + textHeight / 2, textPaint)
            } else if (i < 7) {
                canvas?.drawText(week[i], (cell.rect!!.left + cell.rect!!.right) / 2, (cell.rect!!.bottom + cell.rect!!.top) / 2 + textHeight / 2, textPaint)
            }
        }
    }

    var downX : Float = 0f

    var downY : Float = 0f

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action){
            MotionEvent.ACTION_CANCEL -> return false
            MotionEvent.ACTION_MOVE -> return false
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action){

            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                return false
            }
            MotionEvent.ACTION_UP ->{
                checkPressPosition()
                invalidate()
                return super.onTouchEvent(event)
            }
            MotionEvent.ACTION_CANCEL -> {
                return false
            }
        }
        return super.onTouchEvent(event)
    }

    private fun checkPressPosition(){
        for (cell in cellList){
            if (cell.date[0] != 0){
                cell.isChecked =(downX > cell.rect!!.left && downX < cell.rect!!.right
                        && downY > cell.rect!!.top && downY < cell.rect!!.bottom )
            }
        }
    }

    //初始化cell的值
    private fun initCells(changed: Boolean) {
        if (!changed) return
        val lineGap = dpToPx(2f, context)
        calendar.set(currentDate[0], currentDate[1] - 1, 1)
        //当前月第一天是周几
        val firstDayWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        Log.d(TAG, "yearmonthday" + year + month + day)
        Log.d(TAG, "firstDayWeek" + firstDayWeek)
        titleHeight = (height - paddingTop - paddingBottom) / 7
        val cellWidth = (width - paddingLeft - paddingRight ) / columnNum
        val cellHeight = (height - paddingTop - paddingBottom - titleHeight) / lineNum
        for (j in 0 until lineNum) {
            for (i in 0 until columnNum) {
                var cell = CalendarCell()
                cell.rect = RectF(cellWidth * i + paddingLeft + lineGap / 2,
                        cellHeight * j + paddingTop + lineGap / 2 + titleHeight,
                        cellWidth * (i + 1) + paddingLeft - lineGap / 2,
                        cellHeight * (j + 1) + paddingTop - lineGap / 2 +titleHeight)
                cellList.add(cell)
            }

        }
        val monthDayCount = getMonthDayCount(currentDate[0], currentDate[1])
        for (i in 0 until monthDayCount) {
            if (6 + firstDayWeek + i < cellList.size) {
                cellList[6 + firstDayWeek + i].date = arrayOf(currentDate[0], currentDate[1], i + 1)
                if (i + 1 == currentDate[2]) {
                    cellList[6 + firstDayWeek + i].isToday = true
                    val rect = cellList[6 + firstDayWeek + i].rect
                    pointToday.x = (rect!!.left + rect.right) / 2
                    pointToday.y = (rect!!.top + rect.bottom) / 2
                }
            }
        }

        maxCircleRadius = Math.min(cellHeight, cellWidth).toFloat() / 2.5f
        Log.d(TAG, "maxCircleRadius" + maxCircleRadius)
        animator = ObjectAnimator.ofFloat(0f, maxCircleRadius)
        animator?.addUpdateListener( { var1 ->
            todayCircleRadius = maxCircleRadius * var1.animatedFraction
            invalidate()
        })
        animator?.duration = 1200
        animator?.interpolator = BounceInterpolator()
        animator?.start()
    }

    private fun getMonthDayCount(year: Int, month: Int): Int {
        when (month) {
            1, 3, 5, 7, 8, 10, 12 -> return 31
            4, 6, 9, 11 -> return 30
            2 -> if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                return 29
            } else {
                return 28
            }
            else -> return 0
        }
    }

    inner class CalendarCell {
        var rect: RectF? = null
        var isToday: Boolean = false
        var isChecked :Boolean = false
        var date = arrayOf(0, 0, 0)
    }


}