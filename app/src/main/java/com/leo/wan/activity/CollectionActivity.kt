package com.leo.wan.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.leo.wan.CollectEvent
import com.leo.wan.R
import com.leo.wan.adapter.CollectionAdapter
import com.leo.wan.base.BaseBean
import com.leo.wan.base.NetWorkManager
import com.leo.wan.model.CollectionBean
import com.leo.wan.toast
import com.leo.wan.toastError
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_collection.*
import org.greenrobot.eventbus.EventBus

/**
 * @Description:    当前只支持收藏首页与公众号内的文章，其他未做支持
 * @Author:         ch
 * @CreateDate:     2019/9/3 13:39
 */
class CollectionActivity : BaseActivity() {

    var page: Int = 0
    var collectionList = ArrayList<CollectionBean.DataBean>()

    private val collectionAdapter: CollectionAdapter by lazy {
        CollectionAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)
        initView()
    }

    private fun initView() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        rvData.adapter = collectionAdapter
        rvData.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        getCollectionList()
        initRefreshLayout()
        collectionAdapter.setOnItemClickListener { _, _, position ->
            Intent(this, WebActivity::class.java).run {
                putExtra("url", collectionAdapter.datas[position].link)
                putExtra("title", collectionAdapter.datas[position].title)
                startActivity(this)
            }
        }
        collectionAdapter.listener = {
            val tipDialog = AlertDialog.Builder(this).apply {
                this.setTitle(getString(R.string.tip))
                this.setMessage(getString(R.string.collect_sure))
                this.setPositiveButton(getString(R.string.sure)) { _, _ ->
                    cancelCollect(
                        it, collectionAdapter.datas[it].id,
                        collectionAdapter.datas[it].originId
                    )
                }
                this.setNegativeButton(getString(R.string.cancel)) { p0, _ -> p0.dismiss() }
            }
            tipDialog.show()
        }
    }


    private fun initRefreshLayout() {
        refreshLayout?.run {
            setOnLoadMoreListener {
                page += 1
                getCollectionList()
            }
            setOnRefreshListener {
                page = 0
                getCollectionList()
            }
        }
    }

    /**
     * 获取收藏列表
     */
    private fun getCollectionList() {
        NetWorkManager.getNetApi().getCollectList(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseBean<CollectionBean>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(baseBean: BaseBean<CollectionBean>) {
                    refreshLayout.finishLoadMore()
                    refreshLayout.finishRefresh()
                    if (page == 0) {
                        collectionList.clear()
                    }
                    baseBean.data.datas?.let { collectionList.addAll(it) }
                    collectionAdapter.datas = collectionList
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

    /**
     * 取消收藏
     */
    private fun cancelCollect(position: Int, id: Int, originId: Int) {
        NetWorkManager.getNetApi().cancelCollectionByCollectList(id, originId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseBean<Any>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(baseBean: BaseBean<Any>) {
                    EventBus.getDefault().post(CollectEvent(collectionAdapter.datas[position].originId))
                    collectionAdapter.datas.removeAt(position)
                    collectionAdapter.notifyDataSetChanged()
                    toast(getString(R.string.cancel_success))

                }

                override fun onError(e: Throwable) {
                    toastError(e)
                }

                override fun onComplete() {

                }
            })
    }
}
