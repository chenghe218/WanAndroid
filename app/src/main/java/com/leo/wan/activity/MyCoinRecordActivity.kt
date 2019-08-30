package com.leo.wan.activity

import android.content.Intent
import android.os.Bundle
import com.leo.wan.R
import com.leo.wan.adapter.IntegralAdapter
import com.leo.wan.base.BaseBean
import com.leo.wan.base.NetWorkManager
import com.leo.wan.model.IntegralBean
import com.leo.wan.toastError
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_my_coin_record.*

class MyCoinRecordActivity : BaseActivity() {

    lateinit var integralAdapter: IntegralAdapter
    var page: Int = 1
    var integralList = ArrayList<IntegralBean.DataBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_coin_record)
        initView()
    }

    private fun initView() {
        toolbar.run {
            title = intent.getStringExtra("coin")
            inflateMenu(R.menu.menu_rank)
            setOnMenuItemClickListener {
                startActivity(Intent(this@MyCoinRecordActivity, RankingActivity::class.java))
                true
            }
            setNavigationOnClickListener {
                finish()
            }
        }
        initRefreshLayout()
        integralAdapter = IntegralAdapter(this)
        rvData.adapter = integralAdapter
        getIntegralList()
    }

    private fun initRefreshLayout() {
        refreshLayout?.run {
            setOnLoadMoreListener {
                page += 1
                getIntegralList()
            }
            setOnRefreshListener {
                page = 1
                getIntegralList()
            }
        }
    }

    /**
     * 获取个人积分列表
     */
    private fun getIntegralList() {
        NetWorkManager.getNetApi().getCoinList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseBean<IntegralBean>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(baseBean: BaseBean<IntegralBean>) {
                        refreshLayout.finishLoadMore()
                        refreshLayout.finishRefresh()
                        if (page == 1) {
                            integralList.clear()
                        }
                        baseBean.data.datas?.let { integralList.addAll(it) }
                        integralAdapter.datas = integralList
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
