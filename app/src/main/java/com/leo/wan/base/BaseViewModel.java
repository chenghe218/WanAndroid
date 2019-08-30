package com.leo.wan.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/7/30 20:38
 */
public class BaseViewModel extends AndroidViewModel {

    /**
     * 网络是否可用
     */
    protected MutableLiveData<Boolean> isNetworkAvailable =new MutableLiveData<>();

    /**
     * 页面无数据显示
     */
    protected MutableLiveData<Boolean> isNoData=new MutableLiveData<>();

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }


    public MutableLiveData<Boolean> getIsNetworkAvailable() {
        return isNetworkAvailable;
    }

    public void setIsNetworkAvailable(MutableLiveData<Boolean> isNetworkAvailable) {
        this.isNetworkAvailable = isNetworkAvailable;
    }

    public MutableLiveData<Boolean> getIsNoData() {
        return isNoData;
    }

    public void setIsNoData(MutableLiveData<Boolean> isNoData) {
        this.isNoData = isNoData;
    }
}
