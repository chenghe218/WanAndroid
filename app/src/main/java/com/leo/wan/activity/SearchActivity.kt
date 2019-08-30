package com.leo.wan.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.leo.wan.R
import com.leo.wan.adapter.ArticleAdapter
import com.leo.wan.adapter.WeChatDetailAdapter
import com.leo.wan.base.BaseBean
import com.leo.wan.base.NetWorkManager
import com.leo.wan.model.ArticeData
import com.leo.wan.model.HotkeyBean
import com.leo.wan.model.WeChatDetailBean
import com.leo.wan.toastError
import com.leo.wan.util.flowlayout.FlowLayout
import com.leo.wan.util.flowlayout.TagAdapter
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity() {
    lateinit var searchView: SearchView
    private lateinit var mSearchAutoComplete: SearchView.SearchAutoComplete
    var articleList = ArrayList<ArticeData.ArticleBean>()
    var wxList = ArrayList<WeChatDetailBean.DatasBean>()
    var hotList = ArrayList<String>()
    private var page: Int = 0
    private var isSearch: Boolean = false
    var key: String? = null
    //0 搜索所有  1 公众号搜索
    var type: Int = 0
    private var wxId: Int = 0

    lateinit var articleAdapter: ArticleAdapter

    lateinit var weChatDetailAdapter: WeChatDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initView()
        getHotKeyList()
    }

    private fun initView() {
        isSearch = intent.getBooleanExtra("search", false)
        type = intent.getIntExtra("type", 0)
        wxId = intent.getIntExtra("id", 0)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        layoutHot.visibility = View.GONE
        articleAdapter = ArticleAdapter(this)
        weChatDetailAdapter = WeChatDetailAdapter(this)
        if (type == 0) {
            rvData.adapter = articleAdapter
            page = 0
        } else {
            rvData.adapter = weChatDetailAdapter
            page = 1
        }
        articleAdapter.setOnItemClickListener { _, _, position ->
            searchView.clearFocus()
            Intent(this, WebActivity::class.java).run {
                putExtra("url", articleAdapter.datas[position].link)
                putExtra("title", articleAdapter.datas[position].title?.replace("<em class='highlight'>", "")?.replace("</em>", ""))
                startActivity(this)
            }
        }

        weChatDetailAdapter.setOnItemClickListener { _, _, position ->
            Intent(this, WebActivity::class.java).run {
                putExtra("url", weChatDetailAdapter.datas[position].link)
                putExtra("title", weChatDetailAdapter.datas[position].title)
                startActivity(this)
            }
        }
        initRefreshLayout()
        toolbar.setNavigationOnClickListener {
            if (mSearchAutoComplete.isShown) {
                try {
                    mSearchAutoComplete.setText("")
                    val method = searchView.javaClass.getDeclaredMethod("onCloseClicked")
                    method.isAccessible = true
                    method.invoke(searchView)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                finish()
            }
        }
    }


    private fun initRefreshLayout() {
        refreshLayout?.run {
            setRefreshHeader(ClassicsHeader(context))
            setRefreshFooter(ClassicsFooter(context))
            setEnableRefresh(false)
            setEnableLoadMore(false)
            setOnLoadMoreListener {
                page += 1
                if (type == 0) getArticleList() else getWxSearchList()
            }
            setOnRefreshListener {
                if (type == 0) {
                    page = 0
                    articleAdapter.datas.clear()
                    getArticleList()
                } else {
                    page = 1
                    weChatDetailAdapter.datas.clear()
                    getWxSearchList()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        searchView = menu.findItem(R.id.menuSearch).actionView as SearchView
        searchView.run {
            queryHint = getString(R.string.saerch_hint)
            searchView.setOnQueryTextListener(onQueryTextListener)
            searchView.setOnCloseListener(onCloseListener)
            isIconified = !isSearch
            onActionViewExpanded()
        }
        mSearchAutoComplete = searchView.findViewById(R.id.search_src_text) as SearchView.SearchAutoComplete
        searchView.findViewById<View>(R.id.search_plate).background = null
        searchView.findViewById<View>(R.id.submit_area).background = null
        mSearchAutoComplete.let {
            it.setHintTextColor(ContextCompat.getColor(this, R.color.hint_white))
            it.setTextColor(ContextCompat.getColor(this, R.color.white))
        }
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * 热门搜索流式布局
     * TODO: 第二次点击item时，搜索栏无法获取焦点显示点击的内容
     */
    private fun initTagFlowLayout() {
        val mInflater = LayoutInflater.from(this)
        tagFlowLayout.adapter = object : TagAdapter<String>(hotList) {
            override fun getView(parent: FlowLayout?, position: Int, t: String?): View {
                val tv = mInflater.inflate(R.layout.item_search,
                        tagFlowLayout, false) as TextView
                tv.text = t
                return tv
            }
        }
        tagFlowLayout.setOnTagClickListener { _, position, _ ->
            key = hotList[position]
            mSearchAutoComplete.text = Editable.Factory.getInstance().newEditable(key)
            if (type == 0) {
                articleAdapter.datas.clear()
                getArticleList()
            } else {
                weChatDetailAdapter.datas.clear()
                getWxSearchList()
            }
            true
        }
    }


    /**
     * 搜索事件
     */
    private val onQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let {
                key = query
                refreshLayout.setEnableRefresh(true)
                refreshLayout.setEnableLoadMore(true)
                if (type == 0) {
                    articleAdapter.datas.clear()
                    getArticleList()
                } else {
                    weChatDetailAdapter.datas.clear()
                    getWxSearchList()
                }
                searchView.clearFocus()
            }
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean = false
    }

    /**
     *
     */
    private val onCloseListener = SearchView.OnCloseListener {
        if (type == 0)
            articleAdapter.datas.clear() else weChatDetailAdapter.datas.clear()
        layoutHot.visibility = View.VISIBLE
        rvData.visibility = View.GONE
        false
    }

    /**
     * 搜索后列表
     */
    private fun getArticleList() {
        key?.let {
            NetWorkManager.getNetApi().getSearchList(page, it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<BaseBean<ArticeData>> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(articeData: BaseBean<ArticeData>) {
                            layoutHot.visibility = View.GONE
                            rvData.visibility = View.VISIBLE
                            searchView.clearFocus()
                            refreshLayout.finishLoadMore()
                            refreshLayout.finishRefresh()
                            articeData.data.datas?.let { articleList.addAll(it) }
                            articleAdapter.datas = articleList
                            if (page < articeData.data.pageCount)
                                refreshLayout.setEnableLoadMore(true) else refreshLayout.setEnableLoadMore(false)
                        }

                        override fun onError(e: Throwable) {
                            toastError(e)
                        }

                        override fun onComplete() {

                        }
                    })
        }

    }

    /**
     * 热门搜索
     */
    private fun getHotKeyList() {
        NetWorkManager.getNetApi().getHotKeyList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseBean<List<HotkeyBean>>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(basaBean: BaseBean<List<HotkeyBean>>) {
                        layoutHot.visibility = View.VISIBLE
                        for (i in 0 until basaBean.data.size) {
                            hotList.add(basaBean.data[i].name)
                        }
                        initTagFlowLayout()
                    }

                    override fun onError(e: Throwable) {
                        toastError(e)
                    }

                    override fun onComplete() {

                    }
                })
    }

    /**
     * 搜索公众号后列表
     */
    private fun getWxSearchList() {
        key?.let {
            NetWorkManager.getNetApi().getWxSearchList(wxId, page, it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<BaseBean<WeChatDetailBean>> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(baseBean: BaseBean<WeChatDetailBean>) {
                            layoutHot.visibility = View.GONE
                            rvData.visibility = View.VISIBLE
                            searchView.clearFocus()
                            refreshLayout.finishLoadMore()
                            refreshLayout.finishRefresh()
                            baseBean.data.datas?.let { wxList.addAll(it) }
                            weChatDetailAdapter.datas = wxList
                            if (page < baseBean.data.pageCount)
                                refreshLayout.setEnableLoadMore(true) else refreshLayout.setEnableLoadMore(false)
                        }

                        override fun onError(e: Throwable) {
                            toastError(e)
                        }

                        override fun onComplete() {

                        }
                    })
        }

    }

}


