package com.leo.wan.adapter

import android.content.Context
import android.text.Html
import android.widget.ImageView
import com.leo.wan.R
import com.leo.wan.model.TreeDetailBean
import com.leo.wan.util.recyclerview.BaseRecyclerAdapter
import com.leo.wan.util.recyclerview.ViewHolder

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/16 14:43
 */
class TreeDetailAdapter(context: Context) : BaseRecyclerAdapter<TreeDetailBean.DataBean>(context, R.layout.item_tree_detail) {

    lateinit var listener: (Int) -> Unit

    override fun convert(holder: ViewHolder, dataBean: TreeDetailBean.DataBean, position: Int) {
        holder.setText(R.id.tv_author, dataBean.author)
        holder.setText(R.id.tv_time, dataBean.niceDate)
        holder.setText(R.id.tv_title, Html.fromHtml(dataBean.title))
        holder.setText(R.id.tv_type, "${dataBean.superChapterName} / ${dataBean.chapterName}")
        val ivCollection = holder.getView<ImageView>(R.id.iv_collection)
        if (dataBean.collect)
            ivCollection.setImageResource(R.drawable.ic_collection_select) else
            ivCollection.setImageResource(R.drawable.ic_collection_nom)
        ivCollection.setOnClickListener{
            listener(position)
        }
    }
}
