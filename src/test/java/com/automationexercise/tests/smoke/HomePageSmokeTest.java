package com.automationexercise.tests.smoke;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import com.automationexercise.framework.base.BaseTest;
import com.automationexercise.framework.config.ConfigReader;
import com.automationexercise.framework.pages.HomePage;
import org.testng.annotations.Test;

public class HomePageSmokeTest extends BaseTest {

    @Test(groups = "smoke", description = "Home page loads with the expected title")
    public void homePageShouldLoad() {
        HomePage homePage = new HomePage(getDriver()).open();

        assertEquals(homePage.getTitle(), "Automation Exercise");
        assertTrue(homePage.getCurrentUrl().startsWith(ConfigReader.get("base.url")),
                "Unexpected URL: " + homePage.getCurrentUrl());
    }
}