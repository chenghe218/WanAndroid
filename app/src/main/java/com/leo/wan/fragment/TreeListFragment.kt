package com.leo.wan.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.leo.wan.R
import com.leo.wan.activity.WebActivity
import com.leo.wan.adapter.TreeDetailAdapter
import com.leo.wan.base.BaseBean
import com.leo.wan.base.NetWorkManager
import com.leo.wan.model.TreeDetailBean
import com.leo.wan.toastError
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_project_list.*

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
}
