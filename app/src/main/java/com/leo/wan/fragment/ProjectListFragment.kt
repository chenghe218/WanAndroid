package com.leo.wan.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.leo.wan.R
import com.leo.wan.activity.WebActivity
import com.leo.wan.adapter.ProjectAdapter
import com.leo.wan.base.BaseBean
import com.leo.wan.base.NetWorkManager
import com.leo.wan.model.ProjectBean
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
class ProjectListFragment : Fragment() {

    private var typeId: Int = 0
    var page: Int = 1
    lateinit var projectAdapter: ProjectAdapter
    var projectList = ArrayList<ProjectBean.DataBean>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_project_list, null)
    }

    companion object {
        fun newInstance(id: Int): ProjectListFragment {
            val args = Bundle()
            args.putInt("id", id)
            val fragment = ProjectListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initRefreshLayout()
        projectAdapter = ProjectAdapter(context as Activity)
        rvData.adapter = projectAdapter
        typeId = arguments?.getInt("id", 0)!!
        getProjectList()
        projectAdapter.setOnItemClickListener { _, _, position ->
            Intent(context, WebActivity::class.java).run {
                putExtra("url", projectAdapter.datas[position].link)
                putExtra("title", projectAdapter.datas[position].title)
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
     * 获取项目列表
     */
    private fun getProjectList() {
        NetWorkManager.getNetApi().getProjectList(page, typeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseBean<ProjectBean>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(baseBean: BaseBean<ProjectBean>) {
                        refreshLayout?.let {
                            it.finishLoadMore()
                            it.finishRefresh()
                            if (page == 1) {
                                projectList.clear()
                            }
                            baseBean.data.datas?.let { projectList.addAll(it) }
                            projectAdapter.datas = projectList
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
}
