package com.drsoft.android.repository.remote.retrofit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientBuilder<AC> {

    private final AC apiClient;
    private final Interceptor networkInterceptor;

    public RetrofitClientBuilder(String baseUrl, Class<AC> apiClientClass, Interceptor networkInterceptor) {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.apiClient = retrofit.create(apiClientClass);
        this.networkInterceptor = networkInterceptor;
    }

    public AC build() {
        return apiClient;
    }

    private OkHttpClient getHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true);
        if (networkInterceptor != null) {
            builder.addNetworkInterceptor(networkInterceptor);
        }
        return builder.build();
    }
}
