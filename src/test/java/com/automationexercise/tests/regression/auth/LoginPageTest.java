package com.automationexercise.tests.regression.auth;

import static org.testng.Assert.assertTrue;

import com.automationexercise.framework.base.BaseTest;
import com.automationexercise.framework.pages.LoginPage;
import org.testng.annotations.Test;

public class LoginPageTest extends BaseTest {

    @Test(groups = "regression", description = "LOG-001 and REG-002: login and signup sections are shown")
    public void shouldShowLoginAndSignupSections() {
        LoginPage page = new LoginPage(getDriver()).open();

        assertTrue(page.isLoginHeadingVisible(), "'Login to your account' heading is not visible");
        assertTrue(page.isSignupHeadingVisible(), "'New User Signup!' heading is not visible");
    }

    @Test(groups = "regression", description = "Login form shows email, password and the Login button")
    public void shouldShowLoginFormControls() {
        assertTrue(new LoginPage(getDriver()).open().isLoginFormDisplayed(),
                "Login form controls are not all visible");
    }

    @Test(groups = "regression", description = "Signup form shows name, email and the Signup button")
    public void shouldShowSignupFormControls() {
        assertTrue(new LoginPage(getDriver()).open().isSignupFormDisplayed(),
                "Signup form controls are not all visible");
    }
}