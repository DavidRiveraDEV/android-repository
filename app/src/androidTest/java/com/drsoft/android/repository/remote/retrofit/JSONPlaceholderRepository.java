package com.drsoft.android.repository.remote.retrofit;

import com.drsoft.android.repository.remote.ErrorResponseInterceptor;
import com.drsoft.android.repository.remote.model.ErrorResponse;

import io.reactivex.functions.Consumer;

public class JSONPlaceholderRepository extends RetrofitRepository<JSONPlaceholderClient> {

    public JSONPlaceholderRepository(JSONPlaceholderClient client) {
        super(client);
    }

    public void getUser(int userId, Consumer<Object> onSuccess,
                        Consumer<ErrorResponse> onFailure,
                        ErrorResponseInterceptor errorResponseInterceptor) {
        call(getClient().getUser(userId), onSuccess, onFailure, errorResponseInterceptor);
    }
}
