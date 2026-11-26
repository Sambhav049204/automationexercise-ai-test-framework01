package com.automationexercise.tests.explore;

import static org.testng.Assert.assertTrue;

import com.automationexercise.framework.base.BasePage;
import com.automationexercise.framework.base.BaseTest;
import com.automationexercise.framework.model.AccountDetails;
import com.automationexercise.framework.pages.LoginPage;
import com.automationexercise.framework.pages.ProductDetailsPage;
import com.automationexercise.framework.pages.ProductsPage;
import com.automationexercise.framework.pages.SignupPage;
import com.automationexercise.framework.utils.PageExplorer;
import com.automationexercise.framework.utils.XPaths;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

/**
 * Records the pages behind registration and checkout. Excluded from normal runs; run with:
 * mvn test "-Dexcluded.groups=none" "-Dgroups=explore" "-Dtest=FlowExplorationTest"
 * The account flow creates one throwaway account with dummy data and deletes it again, even on failure.
 */
public class FlowExplorationTest extends BaseTest {

    @Test(groups = "explore")
    public void captureSearchWithNoResults() throws IOException {
        ProductsPage products = new ProductsPage(getDriver()).open();

        products.searchFor("zzzzzzzzzz");

        assertTrue(products.hasSearchResultsHeading(), "SEARCHED PRODUCTS heading did not appear");
        assertReportsNotEmpty(List.of(PageExplorer.explore(getDriver(), "search_no_results")));
    }

    @Test(groups = "explore")
    public void captureGuestCartWithAllLinks() throws IOException {
        FlowPage flow = new FlowPage(getDriver());
        List<Path> reports = new ArrayList<>();

        new ProductsPage(getDriver()).open();
        reports.add(PageExplorer.explore(getDriver(), "products_all_anchors"));

        ProductDetailsPage details = new ProductDetailsPage(getDriver()).open(1);
        details.addToCart();
        assertTrue(details.isAddedConfirmationVisible(), "The Added! confirmation did not appear");
        details.openCartFromConfirmation();
        reports.add(PageExplorer.explore(getDriver(), "cart_guest_with_item"));

        flow.clickText("proceed to checkout"); // official test case 14
        flow.awaitText("register / login");
        reports.add(PageExplorer.explore(getDriver(), "checkout_modal_guest"));

        assertReportsNotEmpty(reports);
    }

    @Test(groups = "explore")
    public void captureAccountCheckoutAndDeletionFlow() throws IOException {
        FlowPage flow = new FlowPage(getDriver());
        List<Path> reports = new ArrayList<>();
        String email = "exploration." + UUID.randomUUID() + "@example.com";
        AccountDetails account = AccountDetails.sample();
        boolean accountCreated = false;

        try {
            new LoginPage(getDriver()).open().startSignup("Exploration User", email);
            SignupPage signup = new SignupPage(getDriver());
            assertTrue(signup.isAccountInformationHeadingVisible(), "ENTER ACCOUNT INFORMATION did not appear");

            signup.fillAccount(account);
            accountCreated = true;
            signup.submit();
            flow.awaitAccountCreated();
            reports.add(PageExplorer.explore(getDriver(), "account_created"));

            flow.continueAfterAccountCreated(); // the site logs the new user in at this point
            reports.add(PageExplorer.explore(getDriver(), "logged_in_home"));

            ProductDetailsPage details = new ProductDetailsPage(getDriver()).open(1);
            details.addToCart();
            assertTrue(details.isAddedConfirmationVisible(), "The Added! confirmation did not appear");
            details.openCartFromConfirmation();
            reports.add(PageExplorer.explore(getDriver(), "cart_logged_in"));

            flow.clickText("proceed to checkout");
            flow.awaitText("address details");
            reports.add(PageExplorer.explore(getDriver(), "checkout"));

            String checkoutUrl = flow.url();
            flow.clickText("place order");
            flow.awaitUrlChangeFrom(checkoutUrl);
            reports.add(PageExplorer.explore(getDriver(), "payment"));
        } finally {
            if (accountCreated) {
                try {
                    flow.deleteAccountSigningInIfNeeded(email, account.password());
                    reports.add(PageExplorer.explore(getDriver(), "account_deleted"));
                } catch (RuntimeException e) {
                    log.warn("Could not delete the exploration account automatically: {}", e.getMessage());
                }
            }
        }

        assertReportsNotEmpty(reports);
    }

    private static void assertReportsNotEmpty(List<Path> reports) throws IOException {
        for (Path report : reports) {
            assertTrue(Files.size(report) > 0, "Empty report: " + report);
        }
    }

    /** Steps whose locators are not known yet use the visible texts from the official test cases. */
    private static final class FlowPage extends BasePage {

        // Observed on the "Account Created" page
        private static final By ACCOUNT_CREATED_HEADING = By.cssSelector("[data-qa='account-created']");
        private static final By CONTINUE_BUTTON = By.cssSelector("[data-qa='continue-button']");
        private static final By DELETE_ACCOUNT_LINK = XPaths.containingText("delete account");

        FlowPage(WebDriver driver) {
            super(driver);
        }

        void clickText(String lowerCaseText) {
            click(XPaths.containingText(lowerCaseText));
        }

        void awaitText(String lowerCaseText) {
            waits.waitForVisible(XPaths.containingText(lowerCaseText));
            waitForPageLoad();
        }

        void awaitUrlChangeFrom(String url) {
            waits.waitForUrlChange(url);
            waitForPageLoad();
        }

        String url() {
            return driver.getCurrentUrl();
        }

        void awaitAccountCreated() {
            waits.waitForVisible(ACCOUNT_CREATED_HEADING);
            waitForPageLoad();
        }

        void continueAfterAccountCreated() {
            click(CONTINUE_BUTTON);
            awaitText("logged in as"); // official test case 1
        }

        void deleteAccountSigningInIfNeeded(String email, String password) {
            if (!isDisplayed(DELETE_ACCOUNT_LINK)) {
                new LoginPage(driver).open().login(email, password);
                awaitText("logged in as");
            }
            click(DELETE_ACCOUNT_LINK);
            awaitText("account deleted"); // official test case 1
        }
    }
}