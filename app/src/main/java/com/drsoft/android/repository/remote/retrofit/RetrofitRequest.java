package com.drsoft.android.repository.remote.retrofit;

import androidx.annotation.NonNull;

import com.drsoft.android.repository.remote.ErrorResponseInterceptor;
import com.drsoft.android.repository.remote.entity.Consumer;
import com.drsoft.android.repository.remote.entity.ErrorResponse;

import io.reactivex.Observable;

public class RetrofitRequest<T> {

    private final Observable<T> observable;
    private final Consumer<T> onSuccess;
    private final Consumer<ErrorResponse> onError;
    private final ErrorResponseInterceptor errorResponseInterceptor;

    public RetrofitRequest(@NonNull Observable<T> observable,
                           Consumer<T> onSuccess,
                           Consumer<ErrorResponse> onError,
                           ErrorResponseInterceptor errorResponseInterceptor) {

        this.observable = observable;
        this.onSuccess = onSuccess;
        this.onError = onError;
        this.errorResponseInterceptor = errorResponseInterceptor;
    }

    public Observable<T> getObservable() {
        return observable;
    }

    public Consumer<T> getOnSuccess() {
        return onSuccess;
    }

    public Consumer<ErrorResponse> getOnError() {
        return onError;
    }

    public ErrorResponseInterceptor getErrorResponseInterceptor() {
        return errorResponseInterceptor;
    }

    public static class Builder<T> {

        private final Observable<T> observable;
        private Consumer<T> onSuccess;
        private Consumer<ErrorResponse> onError;
        private ErrorResponseInterceptor errorResponseInterceptor;

        public Builder(@NonNull Observable<T> observable) {
            this.observable = observable;
        }

        public Builder<T> setOnSuccess(Consumer<T> onSuccess) {
            this.onSuccess = onSuccess;
            return this;
        }

        public Builder<T> setOnError(Consumer<ErrorResponse> onError) {
            this.onError = onError;
            return this;
        }

        public Builder<T> setErrorResponseInterceptor(ErrorResponseInterceptor errorResponseInterceptor) {
            this.errorResponseInterceptor = errorResponseInterceptor;
            return this;
        }

        public RetrofitRequest<T> build() {
            return new RetrofitRequest<>(
                    observable,
                    onSuccess,
                    onError,
                    errorResponseInterceptor
            );
        }
    }
}
