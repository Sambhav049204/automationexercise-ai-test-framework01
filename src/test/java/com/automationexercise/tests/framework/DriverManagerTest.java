package com.automationexercise.tests.framework;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import com.automationexercise.framework.config.ConfigReader;
import com.automationexercise.framework.driver.BrowserType;
import com.automationexercise.framework.driver.DriverManager;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DriverManagerTest {

    private static final Logger LOG = LogManager.getLogger(DriverManagerTest.class);

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }

    @DataProvider(name = "browsers")
    public Object[][] browsers() {
        return new Object[][] {{BrowserType.CHROME}, {BrowserType.EDGE}};
    }

    @Test(dataProvider = "browsers")
    public void shouldOpenHomePageInHeadlessBrowser(BrowserType browser) {
        DriverManager.initDriver(browser, true);
        WebDriver driver = DriverManager.getDriver();

        driver.get(ConfigReader.get("base.url"));

        LOG.info("{} opened page with title: {}", browser, driver.getTitle());
        assertTrue(driver.getCurrentUrl().contains("automationexercise.com"),
                "Unexpected URL: " + driver.getCurrentUrl());
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void shouldFailWhenDriverIsNotInitialised() {
        DriverManager.getDriver();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void shouldReleaseDriverAfterQuit() {
        DriverManager.initDriver(BrowserType.CHROME, true);
        DriverManager.quitDriver();
        DriverManager.getDriver();
    }

    @Test
    public void shouldNotShareDriverBetweenThreads() throws Exception {
        DriverManager.initDriver(BrowserType.CHROME, true);
        assertNotNull(DriverManager.getDriver());

        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            executor.submit(() -> DriverManager.getDriver()).get();
            fail("Another thread must not see this thread's driver");
        } catch (ExecutionException e) {
            assertTrue(e.getCause() instanceof IllegalStateException,
                    "Expected IllegalStateException but got: " + e.getCause());
        } finally {
            executor.shutdownNow();
        }
    }
}