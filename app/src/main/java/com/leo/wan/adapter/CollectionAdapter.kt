package com.leo.wan.adapter

import android.content.Context
import android.text.Html
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.leo.wan.R
import com.leo.wan.model.CollectionBean
import com.leo.wan.util.recyclerview.BaseRecyclerAdapter
import com.leo.wan.util.recyclerview.ViewHolder

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/16 14:43
 */
class CollectionAdapter(context: Context) :
    BaseRecyclerAdapter<CollectionBean.DataBean>(context, R.layout.item_collection) {

    lateinit var listener: (Int) -> Unit

    override fun convert(holder: ViewHolder, dataBean: CollectionBean.DataBean, position: Int) {
        holder.setText(R.id.tv_title, Html.fromHtml(dataBean.title))
        holder.setText(R.id.tv_name, dataBean.author)
        holder.setText(R.id.tv_time, dataBean.niceDate)
        val ivPic = holder.getView<ImageView>(R.id.iv_pic)
        ivPic.visibility = View.GONE
        if (!dataBean.envelopePic.isNullOrEmpty()) {
            ivPic.visibility = View.VISIBLE
            Glide.with(mContext).load(dataBean.envelopePic).into(ivPic)
        }
        holder.setText(R.id.tv_type, dataBean.chapterName)
        holder.getView<ImageView>(R.id.iv_collection).setOnClickListener {
            listener(position)
        }

    }
}
