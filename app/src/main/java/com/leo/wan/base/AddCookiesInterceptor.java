package com.leo.wan.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/22 9:51
 */
public class AddCookiesInterceptor implements Interceptor {
    private static final String COOKIE_PREF = "cookies_prefs";
    private Context mContext;

    public AddCookiesInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        String cookie = getCookie(request.url().toString(), request.url().host());
//        if (request.url().toString().contains("lg/todo") ||
//                request.url().toString().contains("lg/coin") ||
//                request.url().toString().contains("lg/collect") ||
//                request.url().toString().contains("lg/uncollect") ||
//                request.url().toString().contains("article/")) {
            if (!TextUtils.isEmpty(cookie)) {
                builder.addHeader("Cookie", cookie);
            }
        //}
        return chain.proceed(builder.build());
    }

    private String getCookie(String url, String domain) {
        SharedPreferences sp = mContext.getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
        if (!TextUtils.isEmpty(url) && sp.contains(url) && !TextUtils.isEmpty(sp.getString(url, ""))) {
            return sp.getString(url, "");
        }
        if (!TextUtils.isEmpty(domain) && sp.contains(domain) && !TextUtils.isEmpty(sp.getString(domain, ""))) {
            return sp.getString(domain, "");
        }

        return null;
    }
}
