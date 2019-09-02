package com.leo.wan.adapter

import android.content.Context
import android.widget.TextView
import com.leo.wan.R
import com.leo.wan.model.NavigationBean
import com.leo.wan.util.recyclerview.BaseRecyclerAdapter
import com.leo.wan.util.recyclerview.ViewHolder

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/16 14:43
 */
class LeftNavigationAdapter(context: Context) : BaseRecyclerAdapter<NavigationBean>(context, R.layout.item_left_navi) {

    override fun convert(holder: ViewHolder, data: NavigationBean, position: Int) {
        val tv = holder.getView<TextView>(R.id.tv_content)
        tv.text = data.name
        if (data.isChoose) tv.setBackgroundColor(getColor(R.color.select_white)) else tv.setBackgroundColor(getColor(R.color.color_f2))
    }

    fun setChoose(position: Int) {
        mDatas.forEach {
            it.isChoose = false
        }
        mDatas[position].isChoose = true
        notifyDataSetChanged()
    }
}
