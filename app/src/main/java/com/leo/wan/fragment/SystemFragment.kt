package com.leo.wan.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.leo.wan.R
import com.leo.wan.activity.TreeActivity
import com.leo.wan.adapter.TreeAdapter
import com.leo.wan.base.BaseBean
import com.leo.wan.base.NetWorkManager
import com.leo.wan.model.TreeBean
import com.leo.wan.toastError
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_system.*

/**
 * @Description:
 * @Author:         ch
 * @CreateDate:     2019/8/12 14:53
 */
class SystemFragment : Fragment() {

    lateinit var treeAdapter: TreeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getTreeList()
        return inflater.inflate(R.layout.fragment_system, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        treeAdapter = TreeAdapter(context as Activity)
        rvData.adapter = treeAdapter
        treeAdapter.setOnItemClickListener { _, _, position ->
            Intent(context, TreeActivity::class.java).run {
                putExtra("data", treeAdapter.datas[position])
                startActivity(this)
            }
        }
        super.onActivityCreated(savedInstanceState)
    }

    private fun getTreeList() {
        NetWorkManager.getNetApi().getTreeList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseBean<List<TreeBean>>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(basaBean: BaseBean<List<TreeBean>>) {
                        treeAdapter.datas = basaBean.data
                    }

                    override fun onError(e: Throwable) {
                        context?.toastError(e)
                    }

                    override fun onComplete() {

                    }
                })
    }
}