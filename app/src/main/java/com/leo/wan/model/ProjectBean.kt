package com.leo.wan.model

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/20 14:25
 */
class ProjectBean {

    /**
     * curPage : 1
     * datas : []
     * offset : 0
     * over : false
     * pageCount : 11
     * size : 15
     * total : 156
     */

    val curPage: Int = 0
    val offset: Int = 0
    val over: Boolean = false
    val pageCount: Int = 0
    val size: Int = 0
    val total: Int = 0
    val datas: List<DataBean>? = null

    class DataBean {

        /**
         * apkLink :
         * author : binaryshao
         * chapterId : 294
         * chapterName : 完整项目
         * collect : false
         * courseId : 13
         * desc : 玩 Android 客户端，采用 kotlin 语言，Material Design 风格，根据 MVVM 架构使用 Jetpack 架构组件搭建了整套框架
         * envelopePic : https://www.wanandroid.com/blogimgs/10491b74-b534-48b1-a5fe-d2ac00e20d2d.png
         * fresh : true
         * id : 8996
         * link : http://www.wanandroid.com/blog/show/2658
         * niceDate : 13小时前
         * origin :
         * prefix :
         * projectLink : https://github.com/binaryshao/WanAndroid-MVVM
         * publishTime : 1566233222000
         * superChapterId : 294
         * superChapterName : 开源项目主Tab
         * tags : [{"name":"项目","url":"/project/list/1?cid=294"}]
         * title : 玩 Android 客户端 MVVM 架构使用 Jetpack 架构组件
         * type : 0
         * userId : -1
         * visible : 1
         * zan : 0
         */

        val apkLink: String? = null
        val author: String? = null
        val chapterId: Int = 0
        val chapterName: String? = null
        var collect: Boolean = false
        val courseId: Int = 0
        val desc: String? = null
        val envelopePic: String? = null
        val fresh: Boolean = false
        val id: Int = 0
        val link: String? = null
        val niceDate: String? = null
        val origin: String? = null
        val prefix: String? = null
        val projectLink: String? = null
        val publishTime: Long = 0
        val superChapterId: Int = 0
        val superChapterName: String? = null
        val title: String? = null
        val type: Int = 0
        val userId: Int = 0
        val visible: Int = 0
        val zan: Int = 0
        val tags: List<TagsBean>? = null


        class TagsBean {
            /**
             * name : 项目
             * url : /project/list/1?cid=294
             */

            val name: String? = null
            val url: String? = null

        }
    }


}
