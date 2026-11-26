package com.automationexercise.framework.pages;

import com.automationexercise.framework.base.BasePage;
import com.automationexercise.framework.model.AccountDetails;
import com.automationexercise.framework.utils.XPaths;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

/** The "ENTER ACCOUNT INFORMATION" page (/signup) reached after entering name and email. */
public class SignupPage extends BasePage {

    private static final By HEADING = XPaths.tagWithText("h2", "Enter Account Information");
    private static final By PASSWORD = By.id("password");
    private static final By FIRST_NAME = By.id("first_name");
    private static final By LAST_NAME = By.id("last_name");
    private static final By COMPANY = By.id("company");
    private static final By ADDRESS1 = By.id("address1");
    private static final By ADDRESS2 = By.id("address2");
    private static final By STATE = By.id("state");
    private static final By CITY = By.id("city");
    private static final By ZIPCODE = By.id("zipcode");
    private static final By MOBILE = By.id("mobile_number");
    private static final By CREATE_ACCOUNT = By.cssSelector("[data-qa='create-account']");

    // Plain text inputs: can be checked for visibility
    private static final List<String> DISPLAYED_FIELD_IDS = List.of(
            "name", "email", "password", "first_name", "last_name", "company",
            "address1", "address2", "state", "city", "zipcode", "mobile_number");
    // Radios, checkboxes and selects are restyled by a plugin, so only their presence is checked
    private static final List<String> PRESENT_FIELD_IDS = List.of(
            "id_gender1", "id_gender2", "days", "months", "years", "country", "newsletter", "optin");

    public SignupPage(WebDriver driver) {
        super(driver);
    }

    public boolean isAccountInformationHeadingVisible() {
        try {
            waits.waitForVisible(HEADING);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Ids and names of expected form fields that are not on the page. Empty means all are present. */
    public List<String> missingFields() {
        List<String> missing = new ArrayList<>();
        for (String id : DISPLAYED_FIELD_IDS) {
            if (!isDisplayed(By.id(id))) {
                missing.add(id);
            }
        }
        for (String id : PRESENT_FIELD_IDS) {
            if (findAll(By.id(id)).isEmpty()) {
                missing.add(id);
            }
        }
        if (!isDisplayed(CREATE_ACCOUNT)) {
            missing.add("create-account button");
        }
        return missing;
    }

    /** Fills the required details. Name and email are already filled from the previous step. */
    public void fillAccount(AccountDetails account) {
        type(PASSWORD, account.password());
        type(FIRST_NAME, account.firstName());
        type(LAST_NAME, account.lastName());
        type(COMPANY, account.company());
        type(ADDRESS1, account.address1());
        type(ADDRESS2, account.address2());
        type(STATE, account.state());
        type(CITY, account.city());
        type(ZIPCODE, account.zipcode());
        type(MOBILE, account.mobileNumber());
    }

    public void submit() {
        click(CREATE_ACCOUNT);
    }
}