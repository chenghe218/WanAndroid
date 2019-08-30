package com.leo.wan.model

/**
 * @Description:
 * @Author:         ch
 * @CreateDate:     2019/8/27 15:08
 */
class RankBean {
    var curPage: Int = 0
    var offset: Int = 0
    var over: Boolean = false
    var pageCount: Int = 0
    var size: Int = 0
    var total: Int = 0
    var datas: List<DataBean>? = null

    class DataBean {
        var username: String? = null
        var coinCount: Int = 0
    }
}