package com.leo.wan.adapter

import android.content.Context
import android.text.Html
import com.bumptech.glide.Glide

import com.leo.wan.R
import com.leo.wan.model.ProjectBean
import com.leo.wan.util.recyclerview.BaseRecyclerAdapter
import com.leo.wan.util.recyclerview.ViewHolder

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/16 14:43
 */
class ProjectAdapter(context: Context) : BaseRecyclerAdapter<ProjectBean.DataBean>(context, R.layout.item_project) {

    override fun convert(holder: ViewHolder, dataBean: ProjectBean.DataBean, position: Int) {
        holder.setText(R.id.tvTitle, Html.fromHtml(dataBean.title))
        holder.setText(R.id.tvTime,dataBean.niceDate)
        holder.setText(R.id.tvContent,dataBean.desc)
        holder.setText(R.id.tvAuthor,dataBean.author)
        Glide.with(mContext).load(dataBean.envelopePic).into(holder.getView(R.id.ivPic))
    }
}