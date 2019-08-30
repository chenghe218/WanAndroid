package com.leo.wan.model

import java.io.Serializable

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/20 15:22
 */
class TreeBean : Serializable {

    /**
     * children : [{"children":[],"courseId":13,"id":60,"name":"Android Studio相关","order":1000,"parentChapterId":150,"visible":1}]
     * courseId : 13
     * id : 150
     * name : 开发环境
     * order : 1
     * parentChapterId : 0
     * visible : 1
     */

    val courseId: Int = 0
    val id: Int = 0
    val name: String? = null
    val order: Int = 0
    val parentChapterId: Int = 0
    val visible: Int = 0
    val children: List<ChildrenBean>? = null

    class ChildrenBean : Serializable{
        /**
         * children : []
         * courseId : 13
         * id : 60
         * name : Android Studio相关
         * order : 1000
         * parentChapterId : 150
         * visible : 1
         */

        val courseId: Int = 0
        val id: Int = 0
        val name: String? = null
        val order: Int = 0
        val parentChapterId: Int = 0
        val visible: Int = 0
        val children: List<*>? = null
    }
}
