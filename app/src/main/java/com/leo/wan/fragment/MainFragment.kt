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
import com.leo.wan.adapter.ArticleAdapter
import com.leo.wan.base.BaseBean
import com.leo.wan.base.NetWorkManager
import com.leo.wan.model.ArticeData
import com.leo.wan.model.Banner
import com.leo.wan.toastError
import com.leo.wan.util.GlideImageLoader
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.listener.OnBannerListener
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * @Description:
 * @Author:         ch
 * @CreateDate:     2019/8/12 14:51
 */
class MainFragment : Fragment(), OnBannerListener {
    var bannerList = mutableListOf<Banner>()
    var bannerImage = mutableListOf<String>()
    var bannerTitle = mutableListOf<String>()
    var articleList = ArrayList<ArticeData.ArticleBean>()
    var articleTopList = ArrayList<ArticeData.ArticleBean>()
    var page: Int = 0

    lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getBannerList()
        getTopArticleList()
        return inflater.inflate(R.layout.fragment_main, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        articleAdapter = ArticleAdapter(context as Activity)
        rvData.adapter = articleAdapter
        articleAdapter.setOnItemClickListener { _, _, position ->
            Intent(context, WebActivity::class.java).run {
                putExtra("url", articleAdapter.datas[position].link)
                putExtra("title", articleAdapter.datas[position].title)
                startActivity(this)
            }
        }
        initRefreshLayout()
    }

    /**
     * 初始化refreshLayout
     */
    private fun initRefreshLayout() {
        refreshLayout?.run {
            setRefreshHeader(ClassicsHeader(context))
            setRefreshFooter(ClassicsFooter(context))
            setOnLoadMoreListener {
                page += 1
                getArticleList()
            }
            setOnRefreshListener {
                page = 0
                articleAdapter.datas.clear()
                getArticleList()
            }
        }
    }

    /**
     * 初始化Banner
     */
    private fun initBanner() {
        banner?.run {
            setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
            setBannerAnimation(Transformer.Default)
            isAutoPlay(true)
            setImageLoader(GlideImageLoader())
            setImages(bannerImage)
            setBannerTitles(bannerTitle)
            setOnBannerListener(this@MainFragment)
            start()
        }
    }

    override fun OnBannerClick(position: Int) {
        Intent(context, WebActivity::class.java).run {
            putExtra("url", bannerList[position].url)
            putExtra("title", bannerList[position].title)
            startActivity(this)
        }
    }

    /**
     * 获取轮播图
     */
    private fun getBannerList() {
        NetWorkManager.getNetApi().getBanner()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseBean<List<Banner>>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(userBean: BaseBean<List<Banner>>) {
                    bannerList = userBean.data as MutableList<Banner>
                    bannerList.forEach {
                        bannerImage.add(it.imagePath)
                        bannerTitle.add(it.title)
                    }
                    initBanner()
                }

                override fun onError(e: Throwable) {
                    context?.toastError(e)
                }

                override fun onComplete() {

                }
            })
    }

    /**
     * 获取置顶文章列表
     */
    private fun getTopArticleList() {
        NetWorkManager.getNetApi().getTopArticleList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseBean<List<ArticeData.ArticleBean>>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(baseBean: BaseBean<List<ArticeData.ArticleBean>>) {
                    articleTopList = baseBean.data as ArrayList<ArticeData.ArticleBean>
                    articleTopList.forEach {
                        it.isTop = 1
                    }
                    getArticleList()
                }

                override fun onError(e: Throwable) {
                    context?.toastError(e)
                }

                override fun onComplete() {

                }
            })
    }

    /**
     * 获取文章列表
     */
    private fun getArticleList() {
        NetWorkManager.getNetApi().getArticleList(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseBean<ArticeData>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(articeData: BaseBean<ArticeData>) {
                    refreshLayout?.let {
                        it.finishLoadMore()
                        it.finishRefresh()
                        if (page == 0) {
                            articleList.clear()
                            articleList.addAll(articleTopList)
                        }
                        articeData.data.datas?.let { articleList.addAll(it) }
                        articleAdapter.datas = articleList
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
