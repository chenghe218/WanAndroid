package com.leo.wan.model

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/22 10:41
 */
class CollectionBean {
    var curPage: Int = 0
    var offset: Int = 0
    var over: Boolean = false
    var pageCount: Int = 0
    var size: Int = 0
    var total: Int = 0
    var datas: List<DataBean>? = null

    class DataBean {
        var author: String? = null
        var chapterName: String? = null
        var desc: String? = null
        var envelopePic: String? = null
        var link: String? = null
        var niceDate: String? = null
        var origin: String? = null
        var id: Int = 0
        var title: String? = null
        var courseId: Int = 0
        var chapterId: Int = 0
        var originId: Int = 0
        var publishTime: Long = 0
        var userId: Int = 0
        var visible: Int = 0
        var zan: Int = 0
    }
}
