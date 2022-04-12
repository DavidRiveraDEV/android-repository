package com.drsoft.android.repository.remote.retrofit;

import androidx.annotation.NonNull;

import com.drsoft.android.repository.remote.model.ErrorResponse;

import java.io.IOException;

import retrofit2.HttpException;
import retrofit2.Response;

public class RetrofitErrorResponseHandler {

    @NonNull
    public static ErrorResponse handle(Throwable error) {
        int statusCode = 500;
        String body = null;
        if (error != null) {
            body = error.toString();
        }
        if (error instanceof HttpException) {
            final HttpException httpException = ((HttpException) error);
            statusCode = httpException.code();
            try {
                final Response<?> response = httpException.response();
                if (response != null && response.errorBody() != null) {
                    body = response.errorBody().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ErrorResponse(statusCode, body);
    }
}
