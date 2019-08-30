package com.leo.wan.model

/**
 * @Description:
 * @Author:         ch
 * @CreateDate:     2019/8/27 14:21
 */
class IntegralBean {
    var curPage: Int = 0
    var offset: Int = 0
    var over: Boolean = false
    var pageCount: Int = 0
    var size: Int = 0
    var total: Int = 0
    var datas: List<DataBean>? = null

    class DataBean {
        var userName: String? = null
        var desc: String? = null
        var date: Long = 0
        var id: Int = 0
        var coinCount: Int = 0
        var type: Int = 0
        var userId: Int = 0
    }
}