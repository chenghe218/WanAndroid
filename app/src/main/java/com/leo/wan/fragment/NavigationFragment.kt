package com.leo.wan.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leo.wan.R
import com.leo.wan.adapter.LeftNavigationAdapter
import com.leo.wan.adapter.RightNavigationAdapter
import com.leo.wan.base.BaseBean
import com.leo.wan.base.NetWorkManager
import com.leo.wan.dismissDialog
import com.leo.wan.model.NavigationBean
import com.leo.wan.showDialog
import com.leo.wan.toastError
import com.leo.wan.util.TopItemDecoration
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_navigation.*


/**
 * @Description:
 * @Author:         ch
 * @CreateDate:     2019/8/12 14:53
 */
class NavigationFragment : Fragment() {

    var leftList = mutableListOf<NavigationBean>()

    private val leftNavigationAdapter: LeftNavigationAdapter by lazy {
        LeftNavigationAdapter(context as Activity)
    }
    private val rightNavigationAdapter: RightNavigationAdapter by lazy {
        RightNavigationAdapter(context as Activity)
    }
    val dialog: ProgressDialog by lazy {
        ProgressDialog(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getNavi()
        return inflater.inflate(R.layout.fragment_navigation, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        rvLeft.adapter = leftNavigationAdapter
        rvRight.adapter = rightNavigationAdapter
        recyclerViewLinkage()
        super.onActivityCreated(savedInstanceState)
    }

    /**
     * 左右两个RecyclerView联动
     */
    private fun recyclerViewLinkage() {
        val manager = rvRight.layoutManager as LinearLayoutManager
        //左边联动右边
        leftNavigationAdapter.setOnItemClickListener { _, _, position ->
            leftNavigationAdapter.setChoose(position)
            manager.scrollToPositionWithOffset(position, 0)
//            TopLinearSmoothScroller(context as Activity).apply {
//                targetPosition = position
//                manager.startSmoothScroll(this)
//            }
        }

        //右边联动左边
        rvRight.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val firstItemPosition = manager.findFirstVisibleItemPosition()
                if (firstItemPosition != -1) {
                    rvLeft.smoothScrollToPosition(firstItemPosition)
                    leftNavigationAdapter.setChoose(firstItemPosition)
                }
            }

        })
    }

    /**
     * 获取导航数据
     */
    private fun getNavi() {
        dialog.showDialog(context as Activity)
        NetWorkManager.getNetApi().getNavi()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseBean<MutableList<NavigationBean>>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(baseBean: BaseBean<MutableList<NavigationBean>>) {
                    dialog.dismissDialog()
                    leftList = baseBean.data
                    leftList[0].isChoose = true
                    leftNavigationAdapter.datas = leftList
                    rightNavigationAdapter.datas = baseBean.data
                    val top = TopItemDecoration(context as Activity).apply {
                        this.tagListener = {
                            leftList[it].name.toString()
                        }
                    }
                    rvRight.addItemDecoration(top)
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