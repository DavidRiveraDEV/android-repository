package com.drsoft.android.repository.remote.retrofit;

import static com.google.common.truth.Truth.assertThat;

import com.drsoft.android.repository.remote.entity.ErrorResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

@RunWith(JUnit4.class)
public class RetrofitErrorResponseHandlerTest {

    @Test
    public void getErrorSuccess() {
        final int statusCode = 500;
        final String bodyString = "{}";
        final ResponseBody responseBody = ResponseBody.create(bodyString, MediaType.parse("application/json"));
        Response<?> response = Response.error(statusCode, responseBody);
        final HttpException httpException = new HttpException(response);

        final ErrorResponse errorResponse = RetrofitErrorResponseHandler.handle(httpException);

        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatusCode()).isEqualTo(statusCode);
        assertThat(errorResponse.getBody()).isEqualTo(bodyString);
    }

    @Test
    public void getErrorSuccessFromNull() {
        final int statusCode = 500;

        final ErrorResponse errorResponse = RetrofitErrorResponseHandler.handle(null);

        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatusCode()).isEqualTo(statusCode);
        assertThat(errorResponse.getBody()).isNull();
    }

    @Test
    public void getErrorSuccessFromNonHttpException() {
        final int statusCode = 500;
        final Exception exception = new NullPointerException("");
        final ErrorResponse errorResponse = RetrofitErrorResponseHandler.handle(exception);

        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatusCode()).isEqualTo(statusCode);
        assertThat(errorResponse.getBody()).isEqualTo("java.lang.NullPointerException: ");
    }
}