package com.core.retry;

import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;

public final class RetryUtils {
    private static final Retry retry = buildRetry();

    private RetryUtils() {
    }

    private static Retry buildRetry() {
        RetryRegistry retryRegistry = RetryRegistry.ofDefaults();
        RetryConfig config = RetryConfig.<Boolean>custom()
            .maxAttempts(3)
            .intervalFunction(IntervalFunction.ofExponentialBackoff(100, 2))
            .build();
        return retryRegistry.retry(RetryUtils.class.getCanonicalName(), config);
    }

    public static Retry getInstance() {
        return retry;
    }
}
