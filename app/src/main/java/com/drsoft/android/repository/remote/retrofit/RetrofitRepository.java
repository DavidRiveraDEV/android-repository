package com.drsoft.android.repository.remote.retrofit;

import androidx.annotation.NonNull;

import com.drsoft.android.repository.remote.ErrorResponseInterceptor;
import com.drsoft.android.repository.remote.model.ErrorResponse;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
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
        ).build();;
    }

    protected C getClient() {
        return client;
    }

    protected <T> void call(@NonNull Observable<T> observable,
                               Consumer<T> onSuccess,
                               Consumer<ErrorResponse> onError,
                               ErrorResponseInterceptor errorResponseInterceptor) {
        new RetrofitClientCaller<>(observable)
                .onSuccess(onSuccess)
                .onError(onError)
                .errorResponseInterceptor(errorResponseInterceptor)
                .execute();
    }
}
