package com.automationexercise.tests.framework;

import static org.testng.Assert.assertTrue;

import com.automationexercise.framework.driver.BrowserType;
import com.automationexercise.framework.driver.DriverManager;
import com.automationexercise.framework.utils.WaitUtils;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class WaitUtilsTest {

    @BeforeMethod
    public void setUp() {
        DriverManager.initDriver(BrowserType.CHROME, true);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }

    @Test(expectedExceptions = TimeoutException.class)
    public void shouldTimeOutWhenElementNeverAppears() {
        DriverManager.getDriver().get(TestPages.dataUrl("<html><body><p>empty</p></body></html>"));
        new WaitUtils(DriverManager.getDriver(), Duration.ofSeconds(1)).waitForVisible(By.id("nope"));
    }

    @Test
    public void shouldWaitUntilElementBecomesInvisible() {
        String html = """
                <html><body>
                  <div id="x">hello</div>
                  <script>setTimeout(function () { document.getElementById('x').style.display = 'none'; }, 800);</script>
                </body></html>
                """;
        DriverManager.getDriver().get(TestPages.dataUrl(html));

        WaitUtils waits = new WaitUtils(DriverManager.getDriver(), Duration.ofSeconds(10));

        assertTrue(waits.waitForInvisible(By.id("x")));
    }
}