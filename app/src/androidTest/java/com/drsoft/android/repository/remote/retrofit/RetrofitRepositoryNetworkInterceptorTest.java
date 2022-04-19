package com.drsoft.android.repository.remote.retrofit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import okhttp3.Request;

@RunWith(JUnit4.class)
public class RetrofitRepositoryNetworkInterceptorTest {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private JSONPlaceholderRepository repository;
    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    @Before
    public void setUp() {
        repository = new JSONPlaceholderRepository(BASE_URL, JSONPlaceholderClient.class,
                chain -> {
                    countDownLatch.countDown();
                    Request request = chain.request();
                    return chain.proceed(request.newBuilder().build());
                });
    }

    @Test
    public void test_networkInterceptor() throws Exception {
        final int userId = 1;

        repository.getUser(userId,
                o -> {},
                error -> {
                    throw new RuntimeException(error.toString());
                }, null);
        if (!countDownLatch.await(5, TimeUnit.SECONDS)) {
            throw new TimeoutException("Request time is over");
        }
    }

    @After
    public void tearDown() {
        repository = null;
    }
}