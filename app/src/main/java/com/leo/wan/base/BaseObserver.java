package com.leo.wan.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/7/31 11:08
 */
public abstract class BaseObserver<T extends BaseBean> extends AtomicReference<Disposable> implements Observer<T>, Disposable {

    protected Context context;

//    public BaseObserver(Context context) {
//        this.context = context;
//    }

    @Override
    public void onNext(T value) {
//        if (context == null)
//            return;
//        if (context instanceof Activity && (((Activity) context).isDestroyed()
//                || ((Activity) context).isFinishing())) {
//            return;
//        }
        if (value != null) {
            if (value.getErrorCode() == HttpStatusConstants.SUCCESS) {
                onSuccess(value);
            } else {
                onFailure(value);
            }
        }
    }

    @Override
    public void onError(Throwable e) {
//        if (context == null)
//            return;
        final BaseBean baseModel = new BaseBean();
        if (e instanceof ApiException) {
            baseModel.setErrorCode(((ApiException) e).getErrorCode());
            baseModel.setErrorMsg(e.getMessage());
        } else if (e instanceof SocketTimeoutException) {
            baseModel.setErrorMsg("网络连接超时，请稍后重试");
        } else if (e instanceof ConnectException) {
            baseModel.setErrorMsg("网络异常，请检查您的网络");
        } else if (e instanceof RuntimeException) {
            baseModel.setErrorMsg(e.getMessage());
        } else {
            baseModel.setErrorMsg("未知错误");
        }
        Log.e("httpRequest error", e.getMessage() + "");

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                onFailure(baseModel);
            }
        });

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void dispose() {
        DisposableHelper.dispose(this);
    }

    @Override
    public boolean isDisposed() {
        return get() == DisposableHelper.DISPOSED;
    }

    public abstract void onSuccess(T t);


    protected void onFailure(BaseBean model) {
//
    }


    public void exitCurrentUser() {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }
}
