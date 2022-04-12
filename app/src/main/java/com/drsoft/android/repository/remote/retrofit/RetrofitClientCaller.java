package com.drsoft.android.repository.remote.retrofit;

import androidx.annotation.NonNull;

import com.drsoft.android.repository.remote.ErrorResponseInterceptor;
import com.drsoft.android.repository.remote.model.ErrorResponse;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RetrofitClientCaller<T> {

    private final CompositeDisposable compositeDisposable;
    private final Observable<T> observable;
    private Consumer<T> onSuccess;
    private Consumer<ErrorResponse> onError;
    private ErrorResponseInterceptor errorResponseInterceptor;

    public RetrofitClientCaller(Observable<T> observable) {
        this.compositeDisposable = new CompositeDisposable();
        this.observable = observable;
    }

    public RetrofitClientCaller<T> onSuccess(Consumer<T> onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public RetrofitClientCaller<T> onError(Consumer<ErrorResponse> onError) {
        this.onError = onError;
        return this;
    }

    public RetrofitClientCaller<T> errorResponseInterceptor(ErrorResponseInterceptor errorResponseInterceptor) {
        this.errorResponseInterceptor = errorResponseInterceptor;
        return this;
    }

    public void execute() {
        Disposable disposable = observable.subscribeOn(Schedulers.computation())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> completeOnSuccess(onSuccess, response),
                        throwable -> completeOnError(onError, throwable));

        compositeDisposable.add(disposable);
    }

    private void completeOnSuccess(Consumer<T> onSuccess, T response) throws Exception {
        if (onSuccess != null) {
            onSuccess.accept(response);
        }
        compositeDisposable.dispose();
    }

    private void completeOnError(Consumer<ErrorResponse> onFail, Throwable throwable) throws Exception {
        final ErrorResponse errorResponse = RetrofitErrorResponseHandler.handle(throwable);
        if (intercept(errorResponse)) {
            onFail.accept(errorResponse);
        }
        compositeDisposable.dispose();
    }

    private boolean intercept(@NonNull ErrorResponse errorResponse) {
        try {
            if (errorResponseInterceptor != null) {
                return errorResponseInterceptor.intercept(errorResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
