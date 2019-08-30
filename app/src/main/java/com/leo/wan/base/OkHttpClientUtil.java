package com.leo.wan.base;

import android.util.Log;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * .
 */
public class OkHttpClientUtil {

    public static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);
        setCertificates(builder);
        OkHttpClient client = builder.build();
        return client;
    }

    public static class HttpLoggingInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            request = builder.build();
            StringBuilder httpRequest = new StringBuilder("url=" + request.toString() + " \nheader=" + request.headers() + "\nparams=");
            if (request.body() instanceof FormBody) {
                FormBody formBody = (FormBody) request.body();
                for (int i = 0; i < formBody.size(); i++) {
                    httpRequest.append(formBody.name(i)).append("=").append(formBody.value(i)).append("&");
                }
            }
            Log.e("httpRequest", httpRequest.toString());

            Response response = chain.proceed(request);
            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();
           // Log.e("httpResponse", "response body:" + content);
            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, content))
                    .build();
        }
    }

    public static void setCertificates(OkHttpClient.Builder builder) {
        try {
            X509TrustManager tm = new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }

                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain, String authType)
                        throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain, String authType)
                        throws java.security.cert.CertificateException {
                }
            };

            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");

            sslContext.init(null, new TrustManager[]{tm}, new SecureRandom());
            builder.sslSocketFactory(sslContext.getSocketFactory(), tm)
                    .hostnameVerifier(hostnameVerifier);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
