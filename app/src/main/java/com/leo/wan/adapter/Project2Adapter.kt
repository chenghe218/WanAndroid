package com.leo.wan.adapter

import android.content.Context
import android.text.Html
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.leo.wan.EmptyUrl
import com.leo.wan.R
import com.leo.wan.model.ProjectBean
import com.leo.wan.util.SPContent
import com.leo.wan.util.SPManager
import com.leo.wan.util.recyclerview.BaseRecyclerAdapter
import com.leo.wan.util.recyclerview.ViewHolder

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/16 14:43
 */
class Project2Adapter(context: Context) :
    BaseRecyclerAdapter<ProjectBean.DataBean>(context, R.layout.item_project2) {

    lateinit var listener: (Int) -> Unit

    override fun convert(holder: ViewHolder, dataBean: ProjectBean.DataBean, position: Int) {

//        val ivPic = holder.getView<ImageView>(R.id.ivPic)
//        val params = ivPic.layoutParams.apply {
//            val mHeight = (120..150).random()
//            height = mHeight.dip2px(mContext)
//        }
//        ivPic.layoutParams = params
        holder.setText(R.id.tvTitle, Html.fromHtml(dataBean.title))
        holder.setText(R.id.tvTime, dataBean.niceDate)
        holder.setText(R.id.tvContent, dataBean.desc)
        holder.setText(R.id.tvAuthor, dataBean.author)
        if (!SPManager.getBoolean(mContext, SPContent.SP_WIFI, false))
            Glide.with(mContext).load(dataBean.envelopePic).into(holder.getView(R.id.ivPic)) else
            Glide.with(mContext).load(EmptyUrl).into(holder.getView(R.id.ivPic))
        val ivCollection = holder.getView<ImageView>(R.id.iv_collection)
        if (dataBean.collect)
            ivCollection.setImageResource(R.drawable.ic_collection_select) else
            ivCollection.setImageResource(R.drawable.ic_collection_nom)
        ivCollection.setOnClickListener {
            listener(position)
        }
    }
}
