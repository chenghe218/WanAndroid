package com.leo.wan.adapter

import android.content.Context
import com.leo.wan.R
import com.leo.wan.model.IntegralBean
import com.leo.wan.toStringDate
import com.leo.wan.util.recyclerview.BaseRecyclerAdapter
import com.leo.wan.util.recyclerview.ViewHolder

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/16 14:43
 */
class IntegralAdapter(context: Context) : BaseRecyclerAdapter<IntegralBean.DataBean>(context, R.layout.item_interal) {

    override fun convert(holder: ViewHolder, dataBean: IntegralBean.DataBean, position: Int) {
        holder.setText(R.id.tv_desc, dataBean.desc?.let { it.substring(20, it.length) })
        holder.setText(R.id.tv_integral, "+ ${dataBean.coinCount}")
        holder.setText(R.id.tv_time, dataBean.date.toStringDate("yyyy-MM-dd"))
    }
}
