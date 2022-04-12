package com.drsoft.android.repository.remote.retrofit;

import static com.google.common.truth.Truth.assertThat;

import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RetrofitClientBuilderTest {

    @Test
    public void buildClientSuccess() {
        final String BASE_URL = "https://jsonplaceholder.typicode.com";

        final Class<JSONPlaceholderClient> clazz = JSONPlaceholderClient.class;

        final RetrofitClientBuilder<JSONPlaceholderClient> retrofitClientBuilder =
                new RetrofitClientBuilder<>(BASE_URL, clazz, null);
        final JSONPlaceholderClient client = retrofitClientBuilder.build();

        assertThat(client).isNotNull();
    }

    @Test
    public void buildClientFailBadUrl() {
        final String BASE_URL = "";

        final Class<JSONPlaceholderClient> clazz = JSONPlaceholderClient.class;

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new RetrofitClientBuilder<>(BASE_URL, clazz, null));

        assertThat(exception).hasMessageThat().contains("Expected URL scheme 'http' or 'https' but no colon was found");
    }

    @Test
    public void buildClientFailNullUrl() {
        final Class<JSONPlaceholderClient> clazz = JSONPlaceholderClient.class;

        Exception exception = assertThrows(NullPointerException.class,
                () -> new RetrofitClientBuilder<>(null, clazz, null));

        assertThat(exception).hasMessageThat().contains("baseUrl == null");
    }
}