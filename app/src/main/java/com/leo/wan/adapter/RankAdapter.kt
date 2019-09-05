package com.leo.wan.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.flyco.labelview.LabelView
import com.leo.wan.R
import com.leo.wan.model.RankBean
import com.leo.wan.toEncryptionString
import com.leo.wan.util.SPContent
import com.leo.wan.util.SPManager
import com.leo.wan.util.recyclerview.BaseRecyclerAdapter
import com.leo.wan.util.recyclerview.ViewHolder

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/16 14:43
 */
class RankAdapter(context: Context) : BaseRecyclerAdapter<RankBean.DataBean>(context, R.layout.item_rank) {

    private val isMe = SPManager.getString(mContext, SPContent.SP_NAME, "")

    override fun convert(holder: ViewHolder, dataBean: RankBean.DataBean, position: Int) {
        val tvNo = holder.getView<TextView>(R.id.tv_no)
        val labelView = holder.getView<LabelView>(R.id.labelView)
        tvNo.text = "${position.plus(1)}"
        holder.setText(R.id.tv_name, dataBean.username)
        holder.setText(R.id.tv_integral, dataBean.coinCount.toString())
        if (isMe.isNotEmpty() && isMe.toEncryptionString() == dataBean.username) labelView.visibility = View.VISIBLE
        else labelView.visibility = View.GONE
        when (position.plus(1)) {
            1 ->
                tvNo.setTextColor(Color.parseColor("#CD2626"))
            2 ->
                tvNo.setTextColor(Color.parseColor("#FF8247"))
            3 ->
                tvNo.setTextColor(Color.parseColor("#CD950C"))
            else ->
                tvNo.setTextColor(ContextCompat.getColor(mContext, R.color.color_9))
        }
    }
}
