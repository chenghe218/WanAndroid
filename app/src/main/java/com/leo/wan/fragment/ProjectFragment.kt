package com.leo.wan.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.leo.wan.R
import com.leo.wan.base.BaseBean
import com.leo.wan.base.NetWorkManager
import com.leo.wan.dismissDialog
import com.leo.wan.model.ProjectTypeBean
import com.leo.wan.showDialog
import com.leo.wan.toastError
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_project.*

/**
 * @Description:
 * @Author:         ch
 * @CreateDate:     2019/8/12 14:52
 */
class ProjectFragment : Fragment() {

    var typeList = ArrayList<ProjectTypeBean>()
    var nameList = ArrayList<String?>()
    private val fragmentList = mutableListOf<Fragment>()
    val dialog: ProgressDialog by lazy {
        ProgressDialog(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getProjectTypeList()
        return inflater.inflate(R.layout.fragment_project, null)
    }

    private fun initView() {
        viewPager.adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment = fragmentList[position]

            override fun getCount(): Int = nameList.size

            override fun getPageTitle(position: Int): CharSequence? = nameList[position]
        }
        tabLayout.setupWithViewPager(viewPager)
    }

    /**
     * 获取项目分类
     */
    private fun getProjectTypeList() {
        dialog.showDialog(context as Activity)
        NetWorkManager.getNetApi().getProjectType()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseBean<List<ProjectTypeBean>>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(baseBean: BaseBean<List<ProjectTypeBean>>) {
                        dialog.dismissDialog()
                        typeList = baseBean.data as ArrayList<ProjectTypeBean>
                        typeList.forEach {
                            nameList.add(it.name)
                            fragmentList.add(ProjectListFragment.newInstance(it.id))
                        }
                        initView()
                    }

                    override fun onError(e: Throwable) {
                        dialog.dismissDialog()
                        context?.toastError(e)
                    }

                    override fun onComplete() {

                    }
                })
    }

}