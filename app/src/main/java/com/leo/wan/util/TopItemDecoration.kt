package com.leo.wan.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leo.wan.R

/**
 * @Description:
 * @Author:         ch
 * @CreateDate:     2019/9/2 13:42
 */
class TopItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val mHeight = 100
    private val mPaint: Paint = Paint()
    private val textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mContext: Context = context
    private val mRound: Rect = Rect()

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        mPaint.apply {
            color = ContextCompat.getColor(mContext, R.color.color_f2)
        }
        textPaint.apply {
            color = ContextCompat.getColor(mContext, R.color.color_6)
            textSize = 40f
        }
        val left = parent.paddingLeft.toFloat()
        val right = (parent.width - parent.paddingRight).toFloat()
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val childView = parent.getChildAt(i)
            val bottom = childView.top.toFloat()
            val top = bottom - mHeight
            c.drawRect(left, top, right, bottom, mPaint)
            val tag = tagListener(parent.getChildAdapterPosition(childView))
            textPaint.getTextBounds(tag, 0, tag.length, mRound)
            c.drawText(tag, left + textPaint.textSize, bottom - mHeight / 2 + mRound.height() / 2, textPaint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
//        if (parent.getChildAdapterPosition(view) != 0)
        outRect.top = mHeight
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val left = parent.paddingLeft.toFloat()
        val right = (parent.width - parent.paddingRight).toFloat()
        val manager = parent.layoutManager as LinearLayoutManager
        val index = manager.findFirstVisibleItemPosition()
        if (index != -1) {
            val childView = parent.findViewHolderForLayoutPosition(index)!!.itemView
            val top = parent.paddingTop.toFloat()
            val tag = tagListener(index)
            var bottom = parent.paddingTop + mHeight.toFloat()
            bottom = Math.min(childView.bottom.toFloat(), bottom)
            c.drawRect(0f, top, right, bottom, mPaint)
            textPaint.getTextBounds(tag, 0, tag.length, mRound)
            c.drawText(tag, left + textPaint.textSize, bottom - mHeight / 2 + mRound.height() / 2, textPaint)
        }
    }

    /**
     * 获取悬停标签
     */
    lateinit var tagListener: (Int) -> String

}