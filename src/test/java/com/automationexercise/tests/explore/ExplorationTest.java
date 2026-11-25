package com.automationexercise.tests.explore;

import static org.testng.Assert.assertTrue;

import com.automationexercise.framework.base.BasePage;
import com.automationexercise.framework.base.BaseTest;
import com.automationexercise.framework.utils.PageExplorer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

/**
 * Captures the real structure of the site's pages and result states. Excluded from normal runs; run with:
 * mvn test "-Dexcluded.groups=none" "-Dgroups=explore"
 * Read-only apart from a failed login attempt, a signup form that is never completed (no account is
 * created) and one product added to a guest cart.
 */
public class ExplorationTest extends BaseTest {

    private record SeedPage(String name, String path) {
    }

    // Paths taken from the site's own navigation links
    private static final List<SeedPage> SEED_PAGES = List.of(
            new SeedPage("home", "/"),
            new SeedPage("login", "/login"),
            new SeedPage("products", "/products"),
            new SeedPage("view_cart", "/view_cart"),
            new SeedPage("contact_us", "/contact_us"),
            new SeedPage("test_cases", "/test_cases"),
            new SeedPage("api_list", "/api_list"));

    @Test(groups = "explore")
    public void capturePublicPages() throws IOException {
        ExplorerPage page = new ExplorerPage(getDriver());
        List<Path> reports = new ArrayList<>();

        for (SeedPage seed : SEED_PAGES) {
            page.visit(seed.path());
            reports.add(PageExplorer.explore(getDriver(), seed.name()));
        }

        page.visit("/products");
        page.visit(page.firstHref("a[href*='/product_details/']"));
        reports.add(PageExplorer.explore(getDriver(), "product_details_first"));

        assertReportsNotEmpty(reports);
    }

    @Test(groups = "explore")
    public void captureResultStates() throws IOException {
        ExplorerPage page = new ExplorerPage(getDriver());
        List<Path> reports = new ArrayList<>();

        page.attemptLoginWithUnknownUser();
        reports.add(PageExplorer.explore(getDriver(), "login_failed"));

        page.startSignupWithNewUser();
        reports.add(PageExplorer.explore(getDriver(), "signup_account_information"));

        page.searchFor("Top");
        reports.add(PageExplorer.explore(getDriver(), "search_results"));

        // Category and brand links are taken from the first product's detail page, where they were observed
        page.visit("/product_details/1");
        String categoryHref = page.firstHref("a[href^='/category_products/']");
        String brandHref = page.firstHref("a[href^='/brand_products/']");

        page.visit(categoryHref);
        reports.add(PageExplorer.explore(getDriver(), "category_first"));

        page.visit(brandHref);
        reports.add(PageExplorer.explore(getDriver(), "brand_first"));

        page.addProductFromDetailsPage();
        reports.add(PageExplorer.explore(getDriver(), "product_details_added"));

        page.visit("/view_cart");
        reports.add(PageExplorer.explore(getDriver(), "view_cart_with_item"));

        assertReportsNotEmpty(reports);
    }

    private static void assertReportsNotEmpty(List<Path> reports) throws IOException {
        for (Path report : reports) {
            assertTrue(Files.size(report) > 0, "Empty report: " + report);
        }
    }

    private static final class ExplorerPage extends BasePage {

        private static final int MAX_LOAD_ATTEMPTS = 3;

        // Locators below were all observed in the first exploration reports
        private static final By LOGIN_EMAIL = By.cssSelector("[data-qa='login-email']");
        private static final By LOGIN_PASSWORD = By.cssSelector("[data-qa='login-password']");
        private static final By LOGIN_BUTTON = By.cssSelector("[data-qa='login-button']");
        private static final By SIGNUP_NAME = By.cssSelector("[data-qa='signup-name']");
        private static final By SIGNUP_EMAIL = By.cssSelector("[data-qa='signup-email']");
        private static final By SIGNUP_BUTTON = By.cssSelector("[data-qa='signup-button']");
        private static final By SEARCH_INPUT = By.id("search_product");
        private static final By SEARCH_BUTTON = By.id("submit_search");
        private static final By ADD_TO_CART = By.cssSelector("button.cart");
        private static final By ADDED_MODAL_TITLE = By.cssSelector(".modal-title");

        ExplorerPage(WebDriver driver) {
            super(driver);
        }

        void visit(String pathOrUrl) {
            if (pathOrUrl.startsWith("http")) {
                open(pathOrUrl);
            } else {
                openPath(pathOrUrl);
            }
            waitForPageLoad();
            awaitRealPage();
        }

        /** The site sometimes answers "heavy load (queue full)" with an empty title; reload until a real page shows. */
        private void awaitRealPage() {
            for (int attempt = 1; attempt <= MAX_LOAD_ATTEMPTS; attempt++) {
                try {
                    waits.waitForNonBlankTitle();
                    return;
                } catch (TimeoutException e) {
                    log.warn("Page not ready (attempt {} of {}), title='{}'", attempt, MAX_LOAD_ATTEMPTS, driver.getTitle());
                    if (attempt == MAX_LOAD_ATTEMPTS) {
                        throw new IllegalStateException(
                                "The site kept answering with an error page: " + driver.getCurrentUrl(), e);
                    }
                    driver.navigate().refresh();
                    waitForPageLoad();
                }
            }
        }

        String firstHref(String cssSelector) {
            List<WebElement> links = findAll(By.cssSelector(cssSelector));
            if (links.isEmpty()) {
                throw new IllegalStateException("No link found for " + cssSelector + " on " + driver.getCurrentUrl());
            }
            return links.get(0).getDomAttribute("href");
        }

        void attemptLoginWithUnknownUser() {
            visit("/login");
            type(LOGIN_EMAIL, "unknown." + UUID.randomUUID() + "@example.com");
            type(LOGIN_PASSWORD, "dummy-password-not-real");
            click(LOGIN_BUTTON);
            waits.waitForVisible(containingText("incorrect")); // message from official test case 3
        }

        void startSignupWithNewUser() {
            visit("/login");
            type(SIGNUP_NAME, "Exploration User");
            type(SIGNUP_EMAIL, "exploration." + UUID.randomUUID() + "@example.com");
            click(SIGNUP_BUTTON);
            waits.waitForVisible(containingText("enter account information")); // official test case 1
            waitForPageLoad();
        }

        void searchFor(String term) {
            visit("/products");
            type(SEARCH_INPUT, term);
            click(SEARCH_BUTTON);
            waits.waitForVisible(containingText("searched products")); // official test case 9
            waitForPageLoad();
        }

        void addProductFromDetailsPage() {
            visit("/product_details/1");
            click(ADD_TO_CART);
            waits.waitForVisible(ADDED_MODAL_TITLE);
        }

        /** Case-insensitive match on an element's own text (the site may upper-case text with CSS). */
        private static By containingText(String lowerCaseText) {
            return By.xpath("//*[not(self::script) and not(self::style) and contains(translate(text(),"
                    + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + lowerCaseText + "')]");
        }
    }
}