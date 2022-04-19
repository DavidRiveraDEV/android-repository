package com.drsoft.android.repository.remote.retrofit;

import com.drsoft.android.repository.remote.ErrorResponseInterceptor;
import com.drsoft.android.repository.remote.entity.Consumer;
import com.drsoft.android.repository.remote.entity.ErrorResponse;

import okhttp3.Interceptor;

public class JSONPlaceholderRepository extends RetrofitRepository<JSONPlaceholderClient> {

    public JSONPlaceholderRepository(JSONPlaceholderClient client) {
        super(client);
    }

    public JSONPlaceholderRepository(String baseUrl, Class<JSONPlaceholderClient> apiClientClass,
                                     Interceptor networkInterceptor) {
        super(baseUrl, apiClientClass, networkInterceptor);
    }

    public void getUser(int userId, Consumer<Object> onSuccess,
                        Consumer<ErrorResponse> onFailure,
                        ErrorResponseInterceptor errorResponseInterceptor) {
        final RetrofitRequest<Object> request =
                new RetrofitRequest.Builder<>(getClient().getUser(userId))
                        .setOnSuccess(onSuccess)
                        .setOnError(onFailure)
                        .setErrorResponseInterceptor(errorResponseInterceptor)
                        .build();
        call(request);
    }
}
