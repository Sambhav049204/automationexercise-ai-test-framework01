package com.automationexercise.framework.pages;

import com.automationexercise.framework.base.BasePage;
import com.automationexercise.framework.components.HeaderComponent;
import com.automationexercise.framework.components.SubscriptionComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** The "Login to your account" / "New User Signup!" page (/login). */
public class LoginPage extends BasePage {

    // data-qa attributes observed on the page
    private static final By LOGIN_EMAIL = By.cssSelector("[data-qa='login-email']");
    private static final By LOGIN_PASSWORD = By.cssSelector("[data-qa='login-password']");
    private static final By LOGIN_BUTTON = By.cssSelector("[data-qa='login-button']");
    private static final By SIGNUP_NAME = By.cssSelector("[data-qa='signup-name']");
    private static final By SIGNUP_EMAIL = By.cssSelector("[data-qa='signup-email']");
    private static final By SIGNUP_BUTTON = By.cssSelector("[data-qa='signup-button']");

    // Heading texts observed on the page
    private static final By LOGIN_HEADING = By.xpath("//h2[normalize-space()='Login to your account']");
    private static final By SIGNUP_HEADING = By.xpath("//h2[normalize-space()='New User Signup!']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        openPath("/login");
        waitForPageLoad();
        return this;
    }

    public HeaderComponent header() {
        return new HeaderComponent(driver);
    }

    public SubscriptionComponent subscription() {
        return new SubscriptionComponent(driver);
    }

    public boolean isLoginHeadingVisible() {
        return isDisplayed(LOGIN_HEADING);
    }

    public boolean isSignupHeadingVisible() {
        return isDisplayed(SIGNUP_HEADING);
    }

    public boolean isLoginFormDisplayed() {
        return isDisplayed(LOGIN_EMAIL) && isDisplayed(LOGIN_PASSWORD) && isDisplayed(LOGIN_BUTTON);
    }

    public boolean isSignupFormDisplayed() {
        return isDisplayed(SIGNUP_NAME) && isDisplayed(SIGNUP_EMAIL) && isDisplayed(SIGNUP_BUTTON);
    }

    public void login(String email, String password) {
        type(LOGIN_EMAIL, email);
        type(LOGIN_PASSWORD, password);
        click(LOGIN_BUTTON);
    }

    public void startSignup(String name, String email) {
        type(SIGNUP_NAME, name);
        type(SIGNUP_EMAIL, email);
        click(SIGNUP_BUTTON);
    }
}