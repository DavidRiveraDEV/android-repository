package com.drsoft.android.repository.remote.retrofit;

import androidx.annotation.NonNull;

import com.drsoft.android.repository.remote.ErrorResponseInterceptor;
import com.drsoft.android.repository.remote.entity.ErrorResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RetrofitClientCaller<T> {

    private final CompositeDisposable compositeDisposable;
    private final RetrofitRequest<T> request;

    public RetrofitClientCaller(RetrofitRequest<T> request) {
        this.compositeDisposable = new CompositeDisposable();
        this.request = request;
    }

    public void execute() {
        Disposable disposable = request.getObservable().subscribeOn(Schedulers.computation())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::completeOnSuccess,
                        this::completeOnError);

        compositeDisposable.add(disposable);
    }

    private void completeOnSuccess(T response) throws Exception {
        final Consumer<T> onSuccess = request.getOnSuccess();
        if (onSuccess != null) {
            onSuccess.accept(response);
        }
        compositeDisposable.dispose();
    }

    private void completeOnError(Throwable throwable) throws Exception {
        final Consumer<ErrorResponse> onFail = request.getOnError();
        final ErrorResponse errorResponse = RetrofitErrorResponseHandler.handle(throwable);
        if (intercept(errorResponse)) {
            onFail.accept(errorResponse);
        }
        compositeDisposable.dispose();
    }

    private boolean intercept(@NonNull ErrorResponse errorResponse) {
        try {
            final ErrorResponseInterceptor errorResponseInterceptor = request.getErrorResponseInterceptor();
            if (errorResponseInterceptor != null) {
                return errorResponseInterceptor.intercept(errorResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
