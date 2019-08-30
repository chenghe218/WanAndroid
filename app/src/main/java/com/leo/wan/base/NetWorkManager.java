package com.leo.wan.base;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/7/31 10:27
 */
public class NetWorkManager {

    public static String BASE_URL = "https://www.wanandroid.com/";

    private static NetWorkManager netWorkManager;
    private static Retrofit retrofit;
    private static NetApi netApi;
    private Context context;

    public static NetWorkManager getInstance() {
        if (netWorkManager == null) {
            synchronized (NetWorkManager.class) {
                if (netWorkManager == null) {
                    netWorkManager = new NetWorkManager();
                }
            }
        }
        return netWorkManager;
    }

    public void init(Context mContext) {
        context = mContext;
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new OkHttpClientUtil.HttpLoggingInterceptor())
                .addInterceptor(new AddCookiesInterceptor(context))
                .addInterceptor(new SaveCookiesInterceptor(context))
                .build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static NetApi getNetApi() {
        if (netApi == null) {
            synchronized (NetApi.class) {
                netApi = retrofit.create(NetApi.class);
            }
        }
        return netApi;
    }


}
