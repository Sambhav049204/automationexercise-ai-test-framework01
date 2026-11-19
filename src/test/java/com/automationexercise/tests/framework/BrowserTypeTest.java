package com.automationexercise.tests.framework;

import static org.testng.Assert.assertEquals;

import com.automationexercise.framework.driver.BrowserType;
import org.testng.annotations.Test;

public class BrowserTypeTest {

    @Test
    public void shouldParseChromeIgnoringCase() {
        assertEquals(BrowserType.fromString("ChRoMe"), BrowserType.CHROME);
    }

    @Test
    public void shouldParseEdgeWithSurroundingWhitespace() {
        assertEquals(BrowserType.fromString("  edge "), BrowserType.EDGE);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldRejectUnsupportedBrowser() {
        BrowserType.fromString("firefox");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldRejectNullBrowser() {
        BrowserType.fromString(null);
    }
}