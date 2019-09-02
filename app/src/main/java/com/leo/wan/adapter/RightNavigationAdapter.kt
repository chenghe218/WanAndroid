package com.leo.wan.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.leo.wan.R
import com.leo.wan.activity.WebActivity
import com.leo.wan.model.NavigationBean
import com.leo.wan.randomColor
import com.leo.wan.util.flowlayout.FlowLayout
import com.leo.wan.util.flowlayout.TagAdapter
import com.leo.wan.util.flowlayout.TagFlowLayout
import com.leo.wan.util.recyclerview.BaseRecyclerAdapter
import com.leo.wan.util.recyclerview.ViewHolder

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/16 14:43
 */
class RightNavigationAdapter(context: Context) :
    BaseRecyclerAdapter<NavigationBean>(context, R.layout.item_right_navi) {

    override fun convert(holder: ViewHolder, dataBean: NavigationBean, position: Int) {
        holder.setText(R.id.tv_content, dataBean.name)
        val tagFlowLayout = holder.getView<TagFlowLayout>(R.id.tagFlowLayout)
        val mInflater = LayoutInflater.from(mContext)
        tagFlowLayout.adapter = object : TagAdapter<NavigationBean.ArticlesBean>(mDatas[position].articles) {
            override fun getView(parent: FlowLayout?, position: Int, t: NavigationBean.ArticlesBean): View {
                val tv = mInflater.inflate(
                    R.layout.item_search,
                    tagFlowLayout, false
                ) as TextView
                tv.text = t.title
                tv.setTextColor(mContext.randomColor())
                return tv
            }
        }

        tagFlowLayout.setOnTagClickListener { _, po, _ ->
            Intent(mContext, WebActivity::class.java).run {
                putExtra("url", mDatas[position].articles[po].link)
                putExtra("title", mDatas[position].articles[po].title)
                mContext.startActivity(this)
            }
            true
        }
    }
}
