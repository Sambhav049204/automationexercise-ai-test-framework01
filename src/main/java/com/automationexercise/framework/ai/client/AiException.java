package com.automationexercise.framework.ai.client;

/** A failed model call. {@link #isRetryable()} says whether trying again can help (rate limit, timeout, server error). */
public class AiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final boolean retryable;

    public AiException(String message, boolean retryable, Throwable cause) {
        super(message, cause);
        this.retryable = retryable;
    }

    public boolean isRetryable() {
        return retryable;
    }
}
