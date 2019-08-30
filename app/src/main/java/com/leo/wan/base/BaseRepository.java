package com.leo.wan.base;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/7/31 11:18
 */
public abstract class BaseRepository {
    /**
     * 请求数据
     * @param observable
     * @param observer
     */
    public  void doRequest(final Observable observable, final BaseObserver observer) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
