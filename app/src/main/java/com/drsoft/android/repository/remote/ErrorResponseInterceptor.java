package com.drsoft.android.repository.remote;

import androidx.annotation.NonNull;

import com.drsoft.android.repository.remote.model.ErrorResponse;

public interface ErrorResponseInterceptor {

    /**
     * @return true if execution must continue
     */
    boolean intercept(@NonNull ErrorResponse errorResponse);
}
