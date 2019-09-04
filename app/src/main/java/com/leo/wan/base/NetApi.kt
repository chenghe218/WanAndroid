package com.leo.wan.base


import com.leo.wan.model.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/7/31 10:34
 */
interface NetApi {

    /**
     * 注册
     * @return
     */
    @POST("user/register")
    fun register(
        @Query("username") name: String,
        @Query("password") password: String,
        @Query("repassword") repassword: String
    ): Observable<BaseBean<Any>>

    /**
     * 登录
     * @return
     */
    @POST("user/login")
    fun login(
        @Query("username") name: String,
        @Query("password") password: String
    ): Observable<BaseBean<UserBean>>

    /**
     * 退出
     * @return
     */
    @GET("user/logout/json")
    fun loginOut(): Observable<BaseBean<Any>>

    /**
     * 轮播图
     * @return
     */
    @POST("banner/json")
    fun getBanner(): Observable<BaseBean<List<Banner>>>

    /**
     * 获取首页文章列表
     * @return
     */
    @GET("article/list/{page}/json")
    fun getArticleList(@Path("page") value: Int): Observable<BaseBean<ArticeData>>

    /**
     * 获取置顶文章列表
     * @return
     */
    @GET("article/top/json")
    fun getTopArticleList(): Observable<BaseBean<List<ArticeData.ArticleBean>>>

    /**
     * 搜索
     * @return
     */
    @POST("article/query/{page}/json")
    fun getSearchList(
        @Path("page") value: Int,
        @Query("k") word: String
    ): Observable<BaseBean<ArticeData>>

    /**
     * 热门搜索
     * @return
     */
    @POST("hotkey/json")
    fun getHotKeyList(): Observable<BaseBean<List<HotkeyBean>>>

    /**
     * 项目分类
     * @return
     */
    @GET("project/tree/json")
    fun getProjectType(): Observable<BaseBean<List<ProjectTypeBean>>>

    /**
     * 项目列表
     * @return
     */
    @GET("project/list/{page}/json")
    fun getProjectList(
        @Path("page") value: Int,
        @Query("cid") id: Int
    ): Observable<BaseBean<ProjectBean>>

    /**
     * 体系分类
     * @return
     */
    @GET("tree/json")
    fun getTreeList(): Observable<BaseBean<List<TreeBean>>>

    /**
     * 体系列表
     * @return
     */
    @GET("article/list/{page}/json")
    fun getTreeList(
        @Path("page") value: Int,
        @Query("cid") id: Int
    ): Observable<BaseBean<TreeDetailBean>>

    /**
     * 获取导航数据
     * @return
     */
    @GET("navi/json")
    fun getNavi(): Observable<BaseBean<MutableList<NavigationBean>>>

    /**
     * 新增一个TODO
     * @return
     */
    @POST("lg/todo/add/json")
    fun addTodo(
        @Query("title") title: String,
        @Query("content") content: String,
        @Query("date") date: String,
        @Query("type") type: Int,
        @Query("priority") priority: Int
    ): Observable<BaseBean<AddTodoBean>>

    /**
     * 更新一个TODO
     * @return
     */
    @POST("lg/todo/update/{id}/json")
    fun modifyTodo(
        @Path("id") value: Int,
        @Query("title") title: String,
        @Query("content") content: String,
        @Query("date") date: String,
        @Query("status") status: Int,
        @Query("type") type: Int,
        @Query("priority") priority: Int
    ): Observable<BaseBean<Any>>

    /**
     * 更新TODO完成状态
     * @return
     */
    @POST("lg/todo/done/{id}/json")
    fun modifyTodoStatus(
        @Path("id") value: Int,
        @Query("status") status: Int
    ): Observable<BaseBean<Any>>

    /**
     * 删除一个TODO
     * @return
     */
    @POST("lg/todo/delete/{id}/json")
    fun deleteTodo(@Path("id") value: Int): Observable<BaseBean<Any>>

    /**
     * 获取TODO列表
     * @return
     */
    @POST("lg/todo/v2/list/{page}/json")
    fun getTodoList(@Path("page") page: Int): Observable<BaseBean<TodoBean>>

    /**
     * 公众号分类
     * @return
     */
    @GET("wxarticle/chapters/json")
    fun getWeChatTypeList(): Observable<BaseBean<List<WeChatTypeBean>>>

    /**
     * 公众号分类列表
     * @return
     */
    @GET("wxarticle/list/{id}/{page}/json")
    fun getWeChatList(
        @Path("id") value: Int,
        @Path("page") page: Int
    ): Observable<BaseBean<WeChatDetailBean>>

    /**
     * 公众号搜索
     * @return
     */
    @GET("wxarticle/list/{id}/{page}/json")
    fun getWxSearchList(
        @Path("id") value: Int,
        @Path("page") page: Int,
        @Query("k") word: String
    ): Observable<BaseBean<WeChatDetailBean>>

    /**
     * 获取个人积分
     * @return
     */
    @GET("lg/coin/getcount/json")
    fun getcount(): Observable<BaseBean<Int>>

    /**
     * 获取个人积分
     * @return
     */
    @GET("lg/coin/userinfo/json")
    fun getCount(): Observable<BaseBean<CoinBean>>

    /**
     * 获取个人积分列表
     * @return
     */
    @GET("lg/coin/list/{page}/json")
    fun getCoinList(@Path("page") page: Int): Observable<BaseBean<IntegralBean>>

    /**
     * 获取积分排行榜列表
     * @return
     */
    @GET("coin/rank/{page}/json")
    fun getRankList(@Path("page") page: Int): Observable<BaseBean<RankBean>>

    /**
     * 获取收藏文章列表
     * @return
     */
    @GET("lg/collect/list/{page}/json")
    fun getCollectList(@Path("page") page: Int): Observable<BaseBean<CollectionBean>>

    /**
     * 收藏文章
     * @return
     */
    @POST("lg/collect/{id}/json")
    fun addCollection(@Path("id") page: Int): Observable<BaseBean<Any>>

    /**
     * 取消收藏(文章列表内)
     * @return
     */
    @POST("lg/uncollect_originId/{id}/json")
    fun cancelCollectionByArticleList(@Path("id") page: Int): Observable<BaseBean<Any>>

    /**
     * 取消收藏(收藏列表内)
     * @return
     */
    @POST("lg/uncollect/{id}/json")
    fun cancelCollectionByCollectList(
        @Path("id") page: Int,
        @Query("originId") originId: Int = -1
    ): Observable<BaseBean<Any>>

    /**
     * 获取收藏网站列表
     * @return
     */
    @GET("lg/collect/usertools/json")
    fun getWebSiteList(): Observable<BaseBean<List<WebSiteBean>>>

    /**
     * 新增收藏网站
     * @return
     */
    @POST("lg/collect/addtool/json")
    fun addWebSite(
        @Query("name") name: String,
        @Query("link") link: String
    ): Observable<BaseBean<Any>>

    /**
     * 修改收藏网站
     * @return
     */
    @POST("lg/collect/updatetool/json")
    fun modifyWebSite(
        @Query("id") id: Int,
        @Query("name") name: String,
        @Query("link") link: String
    ): Observable<BaseBean<Any>>

    /**
     * 删除收藏网站
     * @return
     */
    @POST("lg/collect/deletetool/json")
    fun deleteWebSite(@Query("id") id: Int): Observable<BaseBean<Any>>
}
