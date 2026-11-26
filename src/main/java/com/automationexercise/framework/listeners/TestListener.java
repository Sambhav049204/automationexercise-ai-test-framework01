package com.automationexercise.framework.listeners;

import com.automationexercise.framework.config.ConfigReader;
import com.automationexercise.framework.driver.DriverManager;
import com.automationexercise.framework.utils.ScreenshotUtils;
import com.automationexercise.framework.utils.SiteHealth;
import java.nio.file.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/** Logs failures with full stack traces and saves a screenshot while the browser is still open. */
public class TestListener implements ITestListener {

    private static final Logger LOG = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestFailure(ITestResult result) {
        String name = displayName(result);
        String category = DriverManager.hasDriver() && SiteHealth.isOverloaded(DriverManager.getDriver())
                ? "ENVIRONMENT (site overloaded)"
                : "UNCLASSIFIED";
        LOG.error("FAILED {} [{}]", name, category, result.getThrowable());

        if (ConfigReader.getBoolean("screenshot.on.failure") && DriverManager.hasDriver()) {
            try {
                Path file = ScreenshotUtils.capture(DriverManager.getDriver(), name);
                LOG.info("Screenshot saved: {}", file);
            } catch (RuntimeException e) {
                LOG.warn("Could not capture screenshot for {}: {}", name, e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOG.warn("SKIPPED {} (a skip can also be a test being retried)", displayName(result));
    }

    @Override
    public void onFinish(ITestContext context) {
        LOG.info("Run finished: passed={}, failed={}, skipped={}",
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
    }

    private static String displayName(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName() + "." + result.getMethod().getMethodName();
    }
}