package com.leo.wan.model

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/20 13:55
 */
class TreeDetailBean {

    /**
     * curPage : 1
     * datas : []
     * offset : 0
     * over : false
     * pageCount : 3
     * size : 20
     * total : 41
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
         * author :  ztq
         * chapterId : 60
         * chapterName : Android Studio相关
         * collect : false
         * courseId : 13
         * desc :
         * envelopePic :
         * fresh : false
         * id : 8201
         * link : https://juejin.im/post/5c9f2412f265da30bd3e428c
         * niceDate : 2019-04-06
         * origin :
         * prefix :
         * projectLink :
         * publishTime : 1554538973000
         * superChapterId : 60
         * superChapterName : 开发环境
         * tags : []
         * title : 零报错基于Virtualbox虚拟机搭建Linux(Ubuntu)的Android开发环境
         * type : 0
         * userId : -1
         * visible : 1
         * zan : 0
         */

        var apkLink: String? = null
        var author: String? = null
        var chapterId: Int = 0
        var chapterName: String? = null
        var collect: Boolean = false
        var courseId: Int = 0
        var desc: String? = null
        var envelopePic: String? = null
        var fresh: Boolean = false
        var id: Int = 0
        var link: String? = null
        var niceDate: String? = null
        var origin: String? = null
        var prefix: String? = null
        var projectLink: String? = null
        var publishTime: Long = 0
        var superChapterId: Int = 0
        var superChapterName: String? = null
        var title: String? = null
        var type: Int = 0
        var userId: Int = 0
        var visible: Int = 0
        var zan: Int = 0
        var tags: List<*>? = null
    }

}
