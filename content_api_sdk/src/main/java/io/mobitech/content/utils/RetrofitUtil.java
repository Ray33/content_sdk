package io.mobitech.content.utils;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created on 07-Jun-16.
 */
public class RetrofitUtil {

    private static final String TAG = RetrofitUtil.class.getSimpleName();

    public static OkHttpClient initHttpClient(boolean isWithCache) {
        OkHttpClient okHttpClient;
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();

        System.setProperty("http.agent", "");

        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("User-Agent", "Mozilla/5.0 (Windows NT x.y; Win64; x64; rv:10.0) Gecko/20100101 Firefox/10.0").build();
                return chain.proceed(request);
            }
        };

        okHttpBuilder.addInterceptor(headerInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);

        if (isWithCache) {
            File cacheDir = new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
            Cache cache = new Cache(cacheDir, 1024);
            okHttpBuilder.cache(cache);
        }

        okHttpClient = okHttpBuilder.build();

        return okHttpClient;
    }

    public static HashMap<String, String> createQueryMap(@NonNull String urlString, String userIp) {
        Uri uri = Uri.parse(urlString);
        Set<String> queryParameterNames = uri.getQueryParameterNames();
        HashMap<String, String> queryMap = new HashMap<>();
        for (String queryName : queryParameterNames) {
            String queryParameter = uri.getQueryParameter(queryName);
            if (queryName.equals("agent")){
                queryMap.put(queryName, "Mozilla/5.0 (Windows NT x.y; Win64; x64; rv:10.0) Gecko/20100101 Firefox/10.0");
            }else {
                queryMap.put(queryName, queryParameter);
            }
        }
        queryMap.put("UserIp", userIp);
        return queryMap;
    }
}
