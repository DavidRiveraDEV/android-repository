package com.drsoft.android.repository.remote.retrofit;

import androidx.annotation.NonNull;

import okhttp3.Interceptor;

public class RetrofitRepository<C> {

    private final C client;

    public RetrofitRepository(C client) {
        this.client = client;
    }

    public RetrofitRepository(String baseUrl, Class<C> apiClientClass, Interceptor networkInterceptor) {
        this.client = new RetrofitClientBuilder<>(
                baseUrl,
                apiClientClass,
                networkInterceptor
        ).build();
    }

    protected C getClient() {
        return client;
    }

    protected <T> void call(@NonNull RetrofitRequest<T> request) {
        new RetrofitClientCaller<T>(request).execute();
    }
}
