package com.leo.wan.model

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/16 14:44
 */
class ArticeData {
    var curPage: Int = 0
    var offset: Int = 0
    var pageCount: Int = 0
    var size: Int = 0
    var total: Int = 0
    var over: Boolean = false
    var datas: List<ArticleBean>? = null

    class ArticleBean {


        /**
         * apkLink :
         * author : 郭霖
         * chapterId : 409
         * chapterName : 郭霖
         * collect : false
         * courseId : 13
         * desc :
         * envelopePic :
         * fresh : false
         * id : 8932
         * link : https://mp.weixin.qq.com/s/7PAMm_FPrA0P3jf0tn3yyQ
         * niceDate : 2019-08-13
         * origin :
         * prefix :
         * projectLink :
         * publishTime : 1565625600000
         * superChapterId : 408
         * superChapterName : 公众号
         * tags : [{"name":"公众号","url":"/wxarticle/list/409/1"}]
         * title : 全方位了解8.0系统下的Handler
         * type : 0
         * userId : -1
         * visible : 1
         * zan : 0
         */

        var apkLink: String? = null
        var author: String? = null
        var chapterId: Int = 0
        var chapterName: String? = null
        var isCollect: Boolean = false
        var courseId: Int = 0
        var desc: String? = null
        var envelopePic: String? = null
        var isFresh: Boolean = false
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
        var tags: List<TagsBean>? = null
        var isTop: Int = 0

    }

    class TagsBean {
        /**
         * name : 公众号
         * url : /wxarticle/list/409/1
         */

        var name: String? = null
        var url: String? = null
    }
}
