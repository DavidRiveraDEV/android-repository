package com.drsoft.android.repository.remote.retrofit;

import static com.google.common.truth.Truth.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RunWith(JUnit4.class)
public class RetrofitClientCallerTest {

    private JSONPlaceholderClient client;

    @Before
    public void setUp() {
        final String BASE_URL = "https://jsonplaceholder.typicode.com";

        final Class<JSONPlaceholderClient> clazz = JSONPlaceholderClient.class;

        final RetrofitClientBuilder<JSONPlaceholderClient> retrofitClientBuilder =
                new RetrofitClientBuilder<>(BASE_URL, clazz, null);
        client = retrofitClientBuilder.build();
    }

    @Test
    public void callSuccess() throws Exception {
        final int userId = 1;
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        final RetrofitRequest<Object> retrofitRequest = new RetrofitRequest.Builder<>(client.getUser(userId))
                .setOnSuccess(o -> {
                    assertThat(o).isNotNull();
                    countDownLatch.countDown();
                })
                .setOnError(error -> {
                    throw new RuntimeException(error.toString());
                })
                .setErrorResponseInterceptor(null)
                .build();

        new RetrofitClientCaller<>(retrofitRequest).execute();

        if (!countDownLatch.await(5, TimeUnit.SECONDS)) {
            throw new TimeoutException("Request time is over");
        }
    }

    @Test
    public void callFail() throws Exception {
        final int userId = 0;
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        final RetrofitRequest<Object> retrofitRequest = new RetrofitRequest.Builder<>(client.getUser(userId))
                .setOnSuccess(o -> {
                    throw new RuntimeException("Call success");
                })
                .setOnError(error -> {
                    assertThat(error).isNotNull();
                    countDownLatch.countDown();
                })
                .setErrorResponseInterceptor(null)
                .build();

        new RetrofitClientCaller<>(retrofitRequest).execute();

        if (!countDownLatch.await(5, TimeUnit.SECONDS)) {
            throw new TimeoutException("Request time is over");
        }
    }

    @Test
    public void callFailContinue() throws Exception {
        final int userId = 0;
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        final RetrofitRequest<Object> retrofitRequest = new RetrofitRequest.Builder<>(client.getUser(userId))
                .setOnSuccess(o -> {
                    throw new RuntimeException();
                })
                .setOnError(error -> countDownLatch.countDown())
                .setErrorResponseInterceptor(errorResponse -> true)
                .build();

        new RetrofitClientCaller<>(retrofitRequest).execute();

        if (!countDownLatch.await(5, TimeUnit.SECONDS)) {
            throw new TimeoutException("Request time is over");
        }
    }

    @Test
    public void callFailNotContinue() throws Exception {
        final int userId = 0;
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        final RetrofitRequest<Object> retrofitRequest = new RetrofitRequest.Builder<>(client.getUser(userId))
                .setOnSuccess(o -> {
                    throw new RuntimeException();
                })
                .setOnError(error -> {
                    throw new RuntimeException("The execution continued");
                })
                .setErrorResponseInterceptor(errorResponse -> {
                    countDownLatch.countDown();
                    return false;
                })
                .build();

        new RetrofitClientCaller<>(retrofitRequest).execute();

        if (!countDownLatch.await(5, TimeUnit.SECONDS)) {
            throw new TimeoutException("Request time is over");
        }
    }

    @After
    public void tearDown() {
        client = null;
    }
}