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
        new RetrofitClientCaller<>(client.getUser(userId))
                .onSuccess(o -> {
                    assertThat(o).isNotNull();
                    countDownLatch.countDown();
                })
                .onError(error -> {
                    throw new RuntimeException(error.toString());
                })
                .errorResponseInterceptor(null)
                .execute();

        if (!countDownLatch.await(5, TimeUnit.SECONDS)) {
            throw new TimeoutException("Request time is over");
        }
    }

    @Test
    public void callFail() throws Exception {
        final int userId = 0;
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        new RetrofitClientCaller<>(client.getUser(userId))
                .onSuccess(o -> {
                    throw new RuntimeException("Call success");
                })
                .onError(error -> {
                    assertThat(error).isNotNull();
                    countDownLatch.countDown();
                })
                .errorResponseInterceptor(null)
                .execute();

        if (!countDownLatch.await(5, TimeUnit.SECONDS)) {
            throw new TimeoutException("Request time is over");
        }
    }

    @Test
    public void callFailContinue() throws Exception {
        final int userId = 0;
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        new RetrofitClientCaller<>(client.getUser(userId))
                .onSuccess(o -> {
                    throw new RuntimeException();
                })
                .onError(error -> countDownLatch.countDown())
                .errorResponseInterceptor(errorResponse -> true)
                .execute();

        if (!countDownLatch.await(5, TimeUnit.SECONDS)) {
            throw new TimeoutException("Request time is over");
        }
    }

    @Test
    public void callFailNotContinue() throws Exception {
        final int userId = 0;
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        new RetrofitClientCaller<>(client.getUser(userId))
                .onSuccess(o -> {
                    throw new RuntimeException();
                })
                .onError(error -> {
                    throw new RuntimeException("The execution continued");
                })
                .errorResponseInterceptor(errorResponse -> {
                    countDownLatch.countDown();
                    return false;
                })
                .execute();

        if (!countDownLatch.await(5, TimeUnit.SECONDS)) {
            throw new TimeoutException("Request time is over");
        }
    }

    @After
    public void tearDown() {
        client = null;
    }
}