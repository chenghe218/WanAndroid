package com.leo.wan.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.leo.wan.*
import com.leo.wan.activity.WebActivity
import com.leo.wan.adapter.TreeDetailAdapter
import com.leo.wan.base.BaseBean
import com.leo.wan.base.NetWorkManager
import com.leo.wan.model.TreeDetailBean
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_project_list.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/20 11:12
 */
class TreeListFragment : Fragment() {

    private var typeId: Int = 0
    var page: Int = 0
    lateinit var treeDetailAdapter: TreeDetailAdapter
    var projectList = ArrayList<TreeDetailBean.DataBean>()

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tree_list, null)
    }

    companion object {
        fun newInstance(id: Int): TreeListFragment {
            val args = Bundle()
            args.putInt("id", id)
            val fragment = TreeListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initRefreshLayout()
        treeDetailAdapter = TreeDetailAdapter(context as Activity)
        rvData.adapter = treeDetailAdapter
        typeId = arguments?.getInt("id", 0)!!
        getTreeList()
        treeDetailAdapter.setOnItemClickListener { _, _, position ->
            Intent(context, WebActivity::class.java).run {
                putExtra("url", treeDetailAdapter.datas[position].link)
                putExtra("title", treeDetailAdapter.datas[position].title)
                startActivity(this)
            }
        }
        treeDetailAdapter.listener = {
            if (treeDetailAdapter.datas[it].collect) {
                val tipDialog = AlertDialog.Builder(context as Activity).apply {
                    this.setTitle(getString(R.string.tip))
                    this.setMessage(getString(R.string.collect_sure))
                    this.setPositiveButton(getString(R.string.sure)) { _, _ ->
                        cancelCollect(
                            it,
                            treeDetailAdapter.datas[it].id
                        )
                    }
                    this.setNegativeButton(getString(R.string.cancel)) { p0, _ -> p0.dismiss() }
                }
                tipDialog.show()
            } else {
                addCollect(it, treeDetailAdapter.datas[it].id)
            }
        }
        super.onActivityCreated(savedInstanceState)
    }

    private fun initRefreshLayout() {
        refreshLayout?.run {
            setRefreshHeader(ClassicsHeader(context))
            setRefreshFooter(ClassicsFooter(context))
            setOnLoadMoreListener {
                page += 1
                getTreeList()
            }
            setOnRefreshListener {
                page = 0
                projectList.clear()
                getTreeList()
            }
        }
    }

    /**
     *
     * 收藏页面取消收藏后 主页面也跟随取消收藏(消息发送地址:ArticleCollectionFragment.kt)
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: CollectEvent) {
        if (!treeDetailAdapter.datas.isNullOrEmpty()) {
            for ((position, dataBean) in treeDetailAdapter.datas.withIndex()) {
                if (dataBean.id == event.id) {
                    treeDetailAdapter.datas[position].collect = false
                    treeDetailAdapter.notifyItemChanged(position)
                }
            }
        }
    }

    /**
     * 登录及退出后修改收藏信息(消息发送地址:MainActivity.kt、LoginActivity.kt)
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginEvent(event: LoginEvent) {
        if (!treeDetailAdapter.datas.isNullOrEmpty()) {
            if (!event.isLogin) {
                treeDetailAdapter.datas.forEach {
                    it.collect = false
                    treeDetailAdapter.notifyDataSetChanged()
                }
            } else {
                page = 0
                projectList.clear()
                getTreeList()
            }
        }
    }

    /**
     * 获取体系列表
     */
    private fun getTreeList() {
        NetWorkManager.getNetApi().getTreeList(page, typeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseBean<TreeDetailBean>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(baseBean: BaseBean<TreeDetailBean>) {
                    refreshLayout.finishLoadMore()
                    refreshLayout.finishRefresh()
                    if (page == 0) {
                        projectList.clear()
                    }
                    baseBean.data.datas?.let { projectList.addAll(it) }
                    treeDetailAdapter.datas = projectList
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
    private fun cancelCollect(position: Int, id: Int) {
        NetWorkManager.getNetApi().cancelCollectionByArticleList(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseBean<Any>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(baseBean: BaseBean<Any>) {
                    treeDetailAdapter.datas[position].collect = false
                    treeDetailAdapter.notifyItemChanged(position)
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
                    treeDetailAdapter.datas[position].collect = true
                    treeDetailAdapter.notifyItemChanged(position)
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
