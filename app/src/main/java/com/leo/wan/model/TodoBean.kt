package com.leo.wan.model

import java.io.Serializable

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/22 10:41
 */
class TodoBean : Serializable{
    var curPage: Int = 0
    var offset: Int = 0
    var over: Boolean = false
    var pageCount: Int = 0
    var size: Int = 0
    var total: Int = 0
    var datas: List<DataBean>? = null

    class DataBean :Serializable {

        var completeDate: Any = 0
        var completeDateStr: String? = null
        var content: String? = null
        var dateStr: String? = null
        var date: Long = 0
        var id: Int = 0
        var priority: Int = 0
        var status: Int = 0
        var title: String? = null
        var type: Int = 0
        var userId: Int = 0
    }
}
