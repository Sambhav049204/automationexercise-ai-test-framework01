package com.automationexercise.tests.regression.auth;

import static org.testng.Assert.assertTrue;

import com.automationexercise.framework.base.BaseTest;
import com.automationexercise.framework.pages.LoginPage;
import com.automationexercise.framework.pages.SignupPage;
import java.util.List;
import java.util.UUID;
import org.testng.annotations.Test;

/** These tests stop at the account information form, so no account is ever created. */
public class SignupPageTest extends BaseTest {

    private SignupPage startSignupWithNewEmail() {
        new LoginPage(getDriver()).open()
                .startSignup("Signup Check", "signup.check." + UUID.randomUUID() + "@example.com");
        return new SignupPage(getDriver());
    }

    @Test(groups = "regression", description = "REG-004: valid name and email lead to ENTER ACCOUNT INFORMATION")
    public void shouldShowAccountInformationAfterValidNameAndEmail() {
        SignupPage signup = startSignupWithNewEmail();

        assertTrue(signup.isAccountInformationHeadingVisible(), "ENTER ACCOUNT INFORMATION did not appear");
    }

    @Test(groups = "regression", description = "REG-006: the account form offers all expected fields")
    public void shouldShowAllAccountFormFields() {
        SignupPage signup = startSignupWithNewEmail();
        assertTrue(signup.isAccountInformationHeadingVisible(), "ENTER ACCOUNT INFORMATION did not appear");

        List<String> missing = signup.missingFields();

        assertTrue(missing.isEmpty(), "Missing fields: " + missing);
    }
}