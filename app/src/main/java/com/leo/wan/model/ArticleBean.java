package com.leo.wan.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/7/31 13:14
 */
public class ArticleBean extends BaseObservable {

    /**
     * children : []
     * courseId : 13
     * id : 408
     * name : 鸿洋
     * order : 190000
     * parentChapterId : 407
     * userControlSetTop : false
     * visible : 1
     */

    private int courseId;
    private int id;
    private String name;
    private int order;
    private int parentChapterId;
    private boolean userControlSetTop;
    private int visible;

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(com.leo.wan.BR.name);
    }

    public int getCourseId() {
        return courseId;
    }

    public int getId() {
        return id;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    public int getParentChapterId() {
        return parentChapterId;
    }

    public boolean isUserControlSetTop() {
        return userControlSetTop;
    }

    public int getVisible() {
        return visible;
    }
}
