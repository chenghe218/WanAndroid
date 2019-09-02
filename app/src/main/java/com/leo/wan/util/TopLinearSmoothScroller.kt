package com.leo.wan.util

import android.content.Context
import androidx.recyclerview.widget.LinearSmoothScroller

/**
 * @Description:
 * @Author:         ch
 * @CreateDate:     2019/9/2 9:28
 */
class TopLinearSmoothScroller(context: Context) : LinearSmoothScroller(context) {

    override fun getVerticalSnapPreference() = SNAP_TO_START

}