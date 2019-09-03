package com.leo.wan.adapter

import android.content.Context
import com.leo.wan.R
import com.leo.wan.model.WebSiteBean
import com.leo.wan.util.recyclerview.BaseRecyclerAdapter
import com.leo.wan.util.recyclerview.ViewHolder

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/16 14:43
 */
class WebSiteAdapter(context: Context) : BaseRecyclerAdapter<WebSiteBean>(context, R.layout.item_website) {

    override fun convert(holder: ViewHolder, dataBean: WebSiteBean, position: Int) {
        holder.setText(R.id.tv_title, dataBean.name)
        holder.setText(R.id.tv_content, dataBean.link)
    }
}
