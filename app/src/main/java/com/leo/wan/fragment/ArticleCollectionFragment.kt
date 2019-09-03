package com.leo.wan.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.leo.wan.CollectEvent
import com.leo.wan.R
import com.leo.wan.activity.WebActivity
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
 * @Description:
 * @Author:         ch
 * @CreateDate:     2019/9/3 17:07
 */
class ArticleCollectionFragment : Fragment(){

    companion object{
        fun getInstance(): ArticleCollectionFragment = ArticleCollectionFragment()
    }

    var page: Int = 0
    var collectionList = ArrayList<CollectionBean.DataBean>()

    private val collectionAdapter: CollectionAdapter by lazy {
        CollectionAdapter(context as Activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getCollectionList()
        return inflater.inflate(R.layout.fragment_artice_collection, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rvData.adapter = collectionAdapter
        rvData.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        initRefreshLayout()
        collectionAdapter.setOnItemClickListener { _, _, position ->
            Intent(context, WebActivity::class.java).run {
                putExtra("url", collectionAdapter.datas[position].link)
                putExtra("title", collectionAdapter.datas[position].title)
                startActivity(this)
            }
        }
        collectionAdapter.listener = {
            val tipDialog = AlertDialog.Builder(context as Activity).apply {
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
                    context?.toastError(e)
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
                    context?.toast(getString(R.string.cancel_success))

                }

                override fun onError(e: Throwable) {
                    context?.toastError(e)
                }

                override fun onComplete() {

                }
            })
    }
}