package com.automationexercise.tests.framework;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import com.automationexercise.framework.listeners.RetryAnalyzer;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.testng.annotations.Test;

public class RetryAnalyzerTest {

    @Test
    public void shouldRetryTransientFailureWithinLimit() {
        assertTrue(RetryAnalyzer.shouldRetry(new StaleElementReferenceException("stale"), 0, 1));
    }

    @Test
    public void shouldStopRetryingWhenLimitIsReached() {
        assertFalse(RetryAnalyzer.shouldRetry(new StaleElementReferenceException("stale"), 1, 1));
    }

    @Test
    public void shouldNeverRetryAssertionFailures() {
        assertFalse(RetryAnalyzer.shouldRetry(new AssertionError("wrong value"), 0, 3));
    }

    @Test
    public void shouldNotRetryGenuinelyMissingElements() {
        assertFalse(RetryAnalyzer.shouldRetry(new NoSuchElementException("missing"), 0, 3));
    }

    @Test
    public void shouldFindTransientCauseInsideWrappedException() {
        Throwable wrapped = new RuntimeException("wrapper", new StaleElementReferenceException("stale"));
        assertTrue(RetryAnalyzer.shouldRetry(wrapped, 0, 1));
    }

    @Test
    public void shouldNotRetryWhenThereIsNoFailure() {
        assertFalse(RetryAnalyzer.shouldRetry(null, 0, 3));
    }
}