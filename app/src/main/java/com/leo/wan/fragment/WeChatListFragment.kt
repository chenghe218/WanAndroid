package com.leo.wan.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.leo.wan.*
import com.leo.wan.activity.SearchActivity
import com.leo.wan.activity.WebActivity
import com.leo.wan.adapter.WeChatDetailAdapter
import com.leo.wan.base.BaseBean
import com.leo.wan.base.NetWorkManager
import com.leo.wan.model.WeChatDetailBean
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_project_list.refreshLayout
import kotlinx.android.synthetic.main.fragment_project_list.rvData
import kotlinx.android.synthetic.main.fragment_wechat_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/20 11:12
 */
class WeChatListFragment : Fragment() {

    private var typeId: Int = 0
    var page: Int = 1
    lateinit var weChatDetailAdapter: WeChatDetailAdapter
    var projectList = ArrayList<WeChatDetailBean.DatasBean>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        EventBus.getDefault().register(this)
        return inflater.inflate(R.layout.fragment_wechat_list, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    companion object {
        fun newInstance(id: Int): WeChatListFragment {
            val args = Bundle()
            args.putInt("id", id)
            val fragment = WeChatListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initRefreshLayout()
        weChatDetailAdapter = WeChatDetailAdapter(context as Activity)
        rvData.adapter = weChatDetailAdapter
        typeId = arguments?.getInt("id", 0)!!
        getProjectList()
        weChatDetailAdapter.setOnItemClickListener { _, _, position ->
            Intent(context, WebActivity::class.java).run {
                putExtra("url", weChatDetailAdapter.datas[position].link)
                putExtra("title", weChatDetailAdapter.datas[position].title)
                startActivity(this)
            }
        }
        tvSearch.setOnClickListener {
            Intent(context, SearchActivity::class.java).run {
                putExtra("type", 1)
                putExtra("id", typeId)
                putExtra("search", true)
                startActivity(this)
            }
        }
        weChatDetailAdapter.listener = {
            if (weChatDetailAdapter.datas[it].collect) {
                val tipDialog = AlertDialog.Builder(context as Activity).apply {
                    this.setTitle(getString(R.string.tip))
                    this.setMessage(getString(R.string.collect_sure))
                    this.setPositiveButton(getString(R.string.sure)) { _, _ ->
                        cancelCollect(
                            it,
                            weChatDetailAdapter.datas[it].id
                        )
                    }
                    this.setNegativeButton(getString(R.string.cancel)) { p0, _ -> p0.dismiss() }
                }
                tipDialog.show()
            } else {
                addCollect(it, weChatDetailAdapter.datas[it].id)
            }
        }
        super.onActivityCreated(savedInstanceState)
    }

    /**
     *
     * 收藏页面取消收藏后 主页面也跟随取消收藏(消息发送地址:ArticleCollectionFragment.kt)
     *
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: CollectEvent) {
        if (!weChatDetailAdapter.datas.isNullOrEmpty()) {
            for ((position, dataBean) in weChatDetailAdapter.datas.withIndex()) {
                if (dataBean.id == event.id) {
                    weChatDetailAdapter.datas[position].collect = false
                    weChatDetailAdapter.notifyItemChanged(position)
                }
            }
        }
    }

    /**
     * 登录及退出后修改收藏信息(消息发送地址:MainActivity.kt、LoginActivity.kt)
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginEvent(event: LoginEvent) {
        if (!weChatDetailAdapter.datas.isNullOrEmpty()) {
            if (!event.isLogin) {
                weChatDetailAdapter.datas.forEach {
                    it.collect = false
                    weChatDetailAdapter.notifyDataSetChanged()
                }
            } else {
                page = 1
                projectList.clear()
                getProjectList()
            }
        }
    }

    private fun initRefreshLayout() {
        refreshLayout?.run {
            setRefreshHeader(ClassicsHeader(context))
            setRefreshFooter(ClassicsFooter(context))
            setOnLoadMoreListener {
                page += 1
                getProjectList()
            }
            setOnRefreshListener {
                page = 1
                projectList.clear()
                getProjectList()
            }
        }
    }

    /**
     * 获取公众号列表
     */
    private fun getProjectList() {
        NetWorkManager.getNetApi().getWeChatList(typeId, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseBean<WeChatDetailBean>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(baseBean: BaseBean<WeChatDetailBean>) {
                    refreshLayout?.let {
                        it.finishLoadMore()
                        it.finishRefresh()
                        if (page == 1) {
                            projectList.clear()
                        }
                        baseBean.data.datas?.let { projectList.addAll(it) }
                        weChatDetailAdapter.datas = projectList
                        if (page < baseBean.data.pageCount)
                            it.setEnableLoadMore(true) else it.setEnableLoadMore(false)
                    }

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
    private fun cancelCollect(position: Int, id: Int) {
        NetWorkManager.getNetApi().cancelCollectionByArticleList(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseBean<Any>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(baseBean: BaseBean<Any>) {
                    weChatDetailAdapter.datas[position].collect = false
                    weChatDetailAdapter.notifyItemChanged(position)
                    context?.toast(getString(R.string.cancel_success))

                }

                override fun onError(e: Throwable) {
                    context?.toastError(e)
                }

                override fun onComplete() {

                }
            })
    }

    /**
     * 添加收藏
     */
    private fun addCollect(position: Int, id: Int) {
        NetWorkManager.getNetApi().addCollection(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseBean<Any>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(baseBean: BaseBean<Any>) {
                    weChatDetailAdapter.datas[position].collect = true
                    weChatDetailAdapter.notifyItemChanged(position)
                    context?.toast(getString(R.string.collect_success))

                }

                override fun onError(e: Throwable) {
                    context?.toastError(e)
                }

                override fun onComplete() {

                }
            })
    }
}
