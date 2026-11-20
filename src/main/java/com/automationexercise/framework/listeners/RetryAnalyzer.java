package com.automationexercise.framework.listeners;

import com.automationexercise.framework.config.ConfigReader;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retries a failed test only when the failure looks transient (flaky UI timing),
 * never for assertion failures, and at most {@code retry.count} times.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger LOG = LogManager.getLogger(RetryAnalyzer.class);
    private static final int MAX_CAUSE_DEPTH = 10;

    private static final List<Class<? extends Throwable>> TRANSIENT_FAILURES = List.of(
            StaleElementReferenceException.class,
            ElementClickInterceptedException.class,
            TimeoutException.class);

    private int attempts = 0;

    @Override
    public boolean retry(ITestResult result) {
        int maxRetries = ConfigReader.getInt("retry.count");
        Throwable failure = result.getThrowable();
        if (shouldRetry(failure, attempts, maxRetries)) {
            attempts++;
            LOG.warn("Retrying {} (retry {} of {}) after transient failure: {}",
                    result.getMethod().getMethodName(), attempts, maxRetries, failure);
            return true;
        }
        return false;
    }

    /** Pure decision logic, kept static so it can be unit tested without a browser. */
    public static boolean shouldRetry(Throwable failure, int attemptsSoFar, int maxRetries) {
        return attemptsSoFar < maxRetries && isTransient(failure);
    }

    /** True if the failure, or any of its causes, is a known transient Selenium problem. */
    public static boolean isTransient(Throwable failure) {
        Throwable current = failure;
        for (int depth = 0; current != null && depth < MAX_CAUSE_DEPTH; depth++) {
            for (Class<? extends Throwable> type : TRANSIENT_FAILURES) {
                if (type.isInstance(current)) {
                    return true;
                }
            }
            current = current.getCause();
        }
        return false;
    }
}