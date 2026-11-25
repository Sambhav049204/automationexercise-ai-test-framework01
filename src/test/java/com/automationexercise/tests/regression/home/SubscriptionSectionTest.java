package com.automationexercise.tests.regression.home;

import static org.testng.Assert.assertTrue;

import com.automationexercise.framework.base.BaseTest;
import com.automationexercise.framework.pages.HomePage;
import com.automationexercise.framework.pages.LoginPage;
import org.testng.annotations.Test;

public class SubscriptionSectionTest extends BaseTest {

    @Test(groups = "regression", description = "SUB-006: the home page shows the subscription box")
    public void homePageShouldShowSubscriptionBox() {
        assertTrue(new HomePage(getDriver()).open().subscription().isVisible(),
                "Subscription box not visible on the home page");
    }

    @Test(groups = "regression", description = "SUB-006: the login page shows the subscription box")
    public void loginPageShouldShowSubscriptionBox() {
        assertTrue(new LoginPage(getDriver()).open().subscription().isVisible(),
                "Subscription box not visible on the login page");
    }
}