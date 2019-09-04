package com.leo.wan.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.leo.wan.R
import com.leo.wan.adapter.WebSiteAdapter
import com.leo.wan.base.BaseBean
import com.leo.wan.base.NetWorkManager
import com.leo.wan.dialog.AddWebSiteDialog
import com.leo.wan.dialog.WebSiteTipDialog
import com.leo.wan.model.WebSiteBean
import com.leo.wan.toast
import com.leo.wan.toastError
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_web_collection.*


/**
 * @Description:
 * @Author:         ch
 * @CreateDate:     2019/9/3 17:07
 */
class WebCollectionFragment : Fragment() {

    var list = ArrayList<WebSiteBean>()

    companion object {
        fun getInstance(): WebCollectionFragment = WebCollectionFragment()
    }

    private var child: FragmentManager? = null
    private val webSiteAdapter: WebSiteAdapter by lazy {
        WebSiteAdapter(context as Activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        child = childFragmentManager
        getWebSiteList()
        return inflater.inflate(R.layout.fragment_web_collection, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rvData.adapter = webSiteAdapter
        refreshLayout.run {
            setOnRefreshListener {
                getWebSiteList()
            }
            setEnableLoadMore(false)
        }
        webSiteAdapter.setOnItemClickListener { _, _, position ->
            WebSiteTipDialog().apply {
                copyListener = {
                    val cm = context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
                    val mClipData = ClipData.newPlainText("Label", webSiteAdapter.datas[position].link)
                    cm!!.primaryClip = mClipData
                    context?.toast(getString(R.string.copy_success))
                }
                modifyListener = {
                    AddWebSiteDialog().newInstance(
                        getString(R.string.todo_modify),
                        webSiteAdapter.datas[position].name,
                        webSiteAdapter.datas[position].link
                    ).apply {
                        saveListener = { name, link ->
                            modifyWebSite(position, webSiteAdapter.datas[position].id, name, link)
                        }
                        show(child, "")
                    }
                }
                deleteListener = {
                    deleteWebSite(webSiteAdapter.datas[position].id, position)
                }
                openListener = {
                    Intent().run {
                        action = "android.intent.action.VIEW"
                        data = Uri.parse(webSiteAdapter.datas[position].link)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        setClassName("com.android.browser", "com.android.browser.BrowserActivity")
                        startActivity(this)
                    }
                }
                show(child, "")
            }
        }
        button.setOnClickListener {
            AddWebSiteDialog().newInstance(getString(R.string.todo_add), "", "").apply {
                saveListener = { name, link ->
                    addWebSite(name, link)
                }
                show(child, "")
            }
        }
    }

    /**
     * 获取网站列表
     */
    private fun getWebSiteList() {
        NetWorkManager.getNetApi().getWebSiteList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseBean<List<WebSiteBean>>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(baseBean: BaseBean<List<WebSiteBean>>) {
                    refreshLayout?.finishRefresh()
                    list.clear()
                    list.addAll(baseBean.data)
                    webSiteAdapter.datas = list
                }

                override fun onError(e: Throwable) {
                    context?.toastError(e)
                }

                override fun onComplete() {

                }
            })
    }

    /**
     * 新增网站
     */
    private fun addWebSite(name: String, link: String) {
        NetWorkManager.getNetApi().addWebSite(name, link)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseBean<Any>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(baseBean: BaseBean<Any>) {
                    getWebSiteList()
                }

                override fun onError(e: Throwable) {
                    context?.toastError(e)
                }

                override fun onComplete() {

                }
            })
    }

    /**
     * 编辑网站
     */
    private fun modifyWebSite(position: Int, id: Int, name: String, link: String) {
        NetWorkManager.getNetApi().modifyWebSite(id, name, link)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseBean<Any>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(baseBean: BaseBean<Any>) {
                    webSiteAdapter.datas[position].name = name
                    webSiteAdapter.datas[position].link = link
                    webSiteAdapter.notifyItemChanged(position)
                }

                override fun onError(e: Throwable) {
                    context?.toastError(e)
                }

                override fun onComplete() {

                }
            })
    }

    /**
     * 删除网站
     */
    private fun deleteWebSite(id: Int, position: Int) {
        NetWorkManager.getNetApi().deleteWebSite(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseBean<Any>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(baseBean: BaseBean<Any>) {
                    webSiteAdapter.datas.remove(webSiteAdapter.datas[position])
                    webSiteAdapter.notifyDataSetChanged()
                    Snackbar.make(constraintLayout, getString(R.string.delete_success), Snackbar.LENGTH_SHORT).show()
                }

                override fun onError(e: Throwable) {
                    context?.toastError(e)
                }

                override fun onComplete() {

                }
            })
    }
}