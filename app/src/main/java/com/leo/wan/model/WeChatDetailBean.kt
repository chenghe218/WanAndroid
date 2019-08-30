package com.leo.wan.model

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/23 11:14
 */
class WeChatDetailBean {

    var curPage: Int = 0
    var offset: Int = 0
    var over: Boolean = false
    var pageCount: Int = 0
    var size: Int = 0
    var total: Int = 0
    var datas: List<DatasBean>? = null

    class DatasBean {

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
        var tags: List<TagsBean>? = null

        class TagsBean {

            var name: String? = null
            var url: String? = null

        }
    }
}
