package com.leo.wan.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.flyco.labelview.LabelView
import com.leo.wan.R
import com.leo.wan.model.TodoBean
import com.leo.wan.util.recyclerview.BaseRecyclerAdapter
import com.leo.wan.util.recyclerview.ViewHolder

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/16 14:43
 */
class TodoAdapter(context: Context) : BaseRecyclerAdapter<TodoBean.DataBean>(context, R.layout.item_todo) {

    override fun convert(holder: ViewHolder, dataBean: TodoBean.DataBean, position: Int) {
        holder.setText(R.id.tv_title, dataBean.title)
        holder.setText(R.id.tv_content, dataBean.content)
        holder.setText(R.id.tv_time, dataBean.dateStr)
        val labView = holder.getView<LabelView>(R.id.labelView)
        val tvStatus = holder.getView<TextView>(R.id.tv_status)
        if (dataBean.priority == 1) labView.visibility = View.VISIBLE else labView.visibility = View.GONE
        if (dataBean.status == 0) tvStatus.text = mContext.getString(R.string.undone)
        else tvStatus.text = mContext.getString(R.string.completed)
    }
}
