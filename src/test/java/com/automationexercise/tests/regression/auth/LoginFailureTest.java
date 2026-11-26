package com.automationexercise.tests.regression.auth;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import com.automationexercise.framework.base.BaseTest;
import com.automationexercise.framework.pages.LoginPage;
import java.util.UUID;
import org.testng.annotations.Test;

public class LoginFailureTest extends BaseTest {

    @Test(groups = "regression", description = "LOG-003: an unknown user sees the incorrect-credentials message")
    public void loginWithUnknownUserShouldShowError() {
        LoginPage page = new LoginPage(getDriver()).open();

        page.login("unknown." + UUID.randomUUID() + "@example.com", "dummy-password-not-real");

        assertTrue(page.isLoginErrorVisible(), "The error message did not appear");
        assertEquals(page.loginErrorText(), "Your email or password is incorrect!");
    }
}