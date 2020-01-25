package com.careagle.sdk.helper;


import android.text.TextUtils;

import com.careagle.sdk.Config;
import com.careagle.sdk.helper.okhttp.CacheInterceptor;
import com.careagle.sdk.helper.okhttp.HttpCache;
import com.careagle.sdk.helper.okhttp.TrustManager;
import com.careagle.sdk.utils.JLog;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Horrarndoo on 2017/9/7.
 * <p>
 */

public class RetrofitCreateHelper {
    private static final int TIMEOUT_READ = 20;
    private static final int TIMEOUT_CONNECTION = 10;
    private static Interceptor interceptor = getInterceptor();
    private static Interceptor headerInterceptor = getHeaderInterceptor();
    private static CacheInterceptor cacheInterceptor = new CacheInterceptor();

    private static ClearableCookieJar cookieJar =
            new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Config.getContext()));

    private static OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder()
            //SSL证书
            .sslSocketFactory(TrustManager.getUnsafeOkHttpClient())
            .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
            //打印日志
            .addInterceptor(interceptor)
            .addInterceptor(headerInterceptor)
            //设置Cache拦截器
            .addNetworkInterceptor(cacheInterceptor)
            .addInterceptor(cacheInterceptor)
            .cache(HttpCache.getCache())
            .cookieJar(cookieJar)
            //time out
            .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
            //失败重连
            .retryOnConnectionFailure(true);

    private static Interceptor getHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder method = original.newBuilder()
//                    .header("User-Agent", "Your-App-Name")
                        .header("Content-Type", "application/json;charset=UTF-8")
                        .method(original.method(), original.body());
                if (!TextUtils.isEmpty(Config.token)) {
                    method.header("Authorization", Config.token);
                }
                Request request = method.build();
                return chain.proceed(request);
            }
        };
    }

    private static HttpLoggingInterceptor getInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                JLog.e(message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // 测试
        return interceptor;
    }

    public static <T> T createApi(Class<T> clazz) {
        return createApi(clazz, TIMEOUT_READ);
    }

    public static <T> T createApi(Class<T> clazz, int timeOut) {
        okhttpBuilder.connectTimeout(timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getBaseUrl())
                .client(okhttpBuilder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(clazz);
    }
}

