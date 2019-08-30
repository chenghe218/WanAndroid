package com.leo.wan.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.leo.wan.R
import com.leo.wan.base.BaseBean
import com.leo.wan.base.NetWorkManager
import com.leo.wan.model.ProjectTypeBean
import com.leo.wan.model.WeChatTypeBean
import com.leo.wan.toastError
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_project.*

/**
 * @Description:
 * @Author:         ch
 * @CreateDate:     2019/8/12 14:53
 */
class WeChatFragment : Fragment() {

    var typeList = ArrayList<WeChatTypeBean>()
    var nameList = ArrayList<String?>()
    private val fragmentList = mutableListOf<Fragment>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getProjectTypeList()
        return inflater.inflate(R.layout.fragment_wechat, null)
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
     * 获取公众号分类
     */
    private fun getProjectTypeList() {
        NetWorkManager.getNetApi().getWeChatTypeList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseBean<List<WeChatTypeBean>>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(baseBean: BaseBean<List<WeChatTypeBean>>) {
                        typeList = baseBean.data as ArrayList<WeChatTypeBean>
                        typeList.forEach {
                            nameList.add(it.name)
                            fragmentList.add(WeChatListFragment.newInstance(it.id))
                        }
                        initView()
                    }

                    override fun onError(e: Throwable) {
                        context?.toastError(e)
                    }

                    override fun onComplete() {

                    }
                })
    }
}