package com.automationexercise.tests.framework;

import static org.testng.Assert.fail;

import com.automationexercise.framework.base.BaseTest;
import java.util.concurrent.atomic.AtomicInteger;
import org.openqa.selenium.StaleElementReferenceException;
import org.testng.annotations.Test;

/**
 * Demonstrates failure handling. Excluded from normal runs; run on request with:
 * mvn test "-Dexcluded.groups=none" "-Dgroups=demo"
 */
public class FailureHandlingDemoTest extends BaseTest {

    private static final AtomicInteger ATTEMPTS = new AtomicInteger();

    @Test(groups = "demo")
    public void failureShouldProduceScreenshot() {
        getDriver().get(TestPages.dataUrl("<html><body><h1>Intentional failure</h1></body></html>"));
        fail("Intentional failure to demonstrate screenshot capture");
    }

    @Test(groups = "demo")
    public void transientFailureShouldBeRetriedAndThenPass() {
        if (ATTEMPTS.incrementAndGet() == 1) {
            throw new StaleElementReferenceException("Simulated transient failure");
        }
    }
}