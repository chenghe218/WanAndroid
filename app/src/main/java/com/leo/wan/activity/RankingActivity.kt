package com.leo.wan.activity

import android.os.Bundle
import com.leo.wan.R
import com.leo.wan.adapter.RankAdapter
import com.leo.wan.base.BaseBean
import com.leo.wan.base.NetWorkManager
import com.leo.wan.model.RankBean
import com.leo.wan.toEncryptionString
import com.leo.wan.toast
import com.leo.wan.toastError
import com.leo.wan.util.SPContent
import com.leo.wan.util.SPManager
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_ranking.*
import java.sql.SQLPermission

class RankingActivity : BaseActivity() {

    lateinit var rankAdapter: RankAdapter
    var page: Int = 1
    var rankList = ArrayList<RankBean.DataBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        initView()
    }

    private fun initView() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        initRefreshLayout()
        rankAdapter = RankAdapter(this)
        rvData.adapter = rankAdapter
        getRankList()
    }

    private fun initRefreshLayout() {
        refreshLayout?.run {
            setOnLoadMoreListener {
                page += 1
                getRankList()
            }
            setOnRefreshListener {
                page = 1
                getRankList()
            }
        }
    }

    /**
     * 获取积分榜列表
     */
    private fun getRankList() {
        NetWorkManager.getNetApi().getRankList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseBean<RankBean>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(baseBean: BaseBean<RankBean>) {
                        refreshLayout.finishLoadMore()
                        refreshLayout.finishRefresh()
                        if (page == 1) {
                            rankList.clear()
                        }
                        baseBean.data.datas?.let { rankList.addAll(it) }
                        rankAdapter.datas = rankList
                        if (page < baseBean.data.pageCount)
                            refreshLayout.setEnableLoadMore(true) else refreshLayout.setEnableLoadMore(false)

                    }

                    override fun onError(e: Throwable) {
                        toastError(e)
                    }

                    override fun onComplete() {

                    }
                })
    }
}
