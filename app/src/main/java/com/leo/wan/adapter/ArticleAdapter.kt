package com.leo.wan.adapter

import android.content.Context
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.leo.wan.EmptyUrl

import com.leo.wan.R
import com.leo.wan.model.ArticeData
import com.leo.wan.util.SPContent
import com.leo.wan.util.SPManager
import com.leo.wan.util.recyclerview.BaseRecyclerAdapter
import com.leo.wan.util.recyclerview.ViewHolder

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/16 14:43
 */
class ArticleAdapter(context: Context) : BaseRecyclerAdapter<ArticeData.ArticleBean>(context, R.layout.item_article) {

    lateinit var listener: (Int) -> Unit

    override fun convert(holder: ViewHolder, articeData: ArticeData.ArticleBean, position: Int) {
        val iv = holder.getView<ImageView>(R.id.iv_pic)
        val ivCollection = holder.getView<ImageView>(R.id.iv_collection)
        val isTop = holder.getView<TextView>(R.id.tv_topping)
        val isNew = holder.getView<TextView>(R.id.tv_new)
        val tvType = holder.getView<TextView>(R.id.tv_type1)
        iv.visibility = View.GONE
        isTop.visibility = View.GONE
        isNew.visibility = View.GONE
        tvType.visibility = View.GONE
        holder.setText(R.id.tv_author, articeData.author)
        holder.setText(R.id.tv_time, articeData.niceDate)
        holder.setText(R.id.tv_title, Html.fromHtml(articeData.title))
        if (!articeData.envelopePic.isNullOrEmpty()) {
            iv.visibility = View.VISIBLE
            if (!SPManager.getBoolean(mContext, SPContent.SP_WIFI, false))
                Glide.with(mContext).load(articeData.envelopePic).into(iv) else
                Glide.with(mContext).load(EmptyUrl).into(iv)
        }
        if (articeData.isTop == 1) isTop.visibility = View.VISIBLE
        holder.setText(R.id.tv_type, "${articeData.superChapterName} / ${articeData.chapterName}")
        if (articeData.fresh) {
            isNew.visibility = View.VISIBLE
        }
        if (!articeData.tags.isNullOrEmpty()) {
            tvType.visibility = View.VISIBLE
            tvType.text = articeData.tags?.get(0)?.name
        }
        if (articeData.collect)
            ivCollection.setImageResource(R.drawable.ic_collection_select) else
            ivCollection.setImageResource(R.drawable.ic_collection_nom)
        ivCollection.setOnClickListener {
            listener(position)
        }
    }
}
