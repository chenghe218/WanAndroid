package com.leo.wan.adapter

import android.content.Context
import com.leo.wan.R
import com.leo.wan.model.TreeBean
import com.leo.wan.util.recyclerview.BaseRecyclerAdapter
import com.leo.wan.util.recyclerview.ViewHolder

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/16 14:43
 */
class TreeAdapter(context: Context) : BaseRecyclerAdapter<TreeBean>(context, R.layout.item_tree) {

    override fun convert(holder: ViewHolder, dataBean: TreeBean, position: Int) {
        holder.setText(R.id.tv_title_one, dataBean.name)
        dataBean.children?.let { children ->
            holder.setText(R.id.tv_title_two, children.joinToString("     ",
                            transform = { child -> child.name.toString() })
            )
        }
    }
}
