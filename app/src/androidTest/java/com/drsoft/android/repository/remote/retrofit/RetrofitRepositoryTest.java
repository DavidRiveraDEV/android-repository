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
public class RetrofitRepositoryTest {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private JSONPlaceholderRepository repository;

    @Before
    public void setUp() {
        JSONPlaceholderClient client = new RetrofitClientBuilder<>(
                BASE_URL,
                JSONPlaceholderClient.class,
                null
        ).build();

        repository = new JSONPlaceholderRepository(client);
    }

    @Test
    public void getUserSuccess() throws Exception {
        final int userId = 1;

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        repository.getUser(userId,
                o -> {
                    assertThat(o).isNotNull();
                    countDownLatch.countDown();
                },
                error -> {
                    throw new RuntimeException(error.toString());
                }, null);
        if (!countDownLatch.await(5, TimeUnit.SECONDS)) {
            throw new TimeoutException("Request time is over");
        }
    }

    @Test
    public void getUserNotFound() throws Exception {
        final int userId = 0;

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        repository.getUser(userId,
                o -> {
                    throw new RuntimeException("User found");
                },
                error -> {
                    assertThat(error.getStatusCode()).isEqualTo(404);
                    countDownLatch.countDown();
                }, null);
        if (!countDownLatch.await(5, TimeUnit.SECONDS)) {
            throw new TimeoutException("Request time is over");
        }
    }

    @Test
    public void getUserErrorInterceptorContinue() throws Exception {
        final int userId = 0;

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        repository.getUser(userId,
                o -> {
                    throw new RuntimeException();
                },
                error -> countDownLatch.countDown(),
                errorResponse -> true);
        if (!countDownLatch.await(5, TimeUnit.SECONDS)) {
            throw new TimeoutException("Request time is over");
        }
    }

    @Test
    public void getUserErrorInterceptorNotContinue() throws Exception {
        final int userId = 0;

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        repository.getUser(userId,
                o -> {
                    throw new RuntimeException();
                },
                error -> {
                    throw new RuntimeException("The execution continued");
                }, errorResponse -> {
                    countDownLatch.countDown();
                    return false;
                });
        if (!countDownLatch.await(5, TimeUnit.SECONDS)) {
            throw new TimeoutException("Request time is over");
        }
    }

    @After
    public void tearDown() {
        repository = null;
    }
}