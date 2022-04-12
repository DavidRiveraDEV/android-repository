package com.drsoft.android.repository.remote.retrofit;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JSONPlaceholderClient {

    @GET("todos/{user_id}")
    Observable<Object> getUser(@Path("user_id") int userId);
}
