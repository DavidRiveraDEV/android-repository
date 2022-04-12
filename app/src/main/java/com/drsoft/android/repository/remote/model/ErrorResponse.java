package com.drsoft.android.repository.remote.model;

import androidx.annotation.NonNull;

public class ErrorResponse {

    private final int statusCode;
    private final String body;

    public ErrorResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    @NonNull
    @Override
    public String toString() {
        return "ErrorResponse{" +
                "statusCode=" + statusCode +
                ", body='" + body + '\'' +
                '}';
    }
}
