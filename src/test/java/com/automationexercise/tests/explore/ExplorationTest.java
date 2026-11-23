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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

/**
 * Captures the real structure of the public pages. Excluded from normal runs; run on request with:
 * mvn test "-Dexcluded.groups=none" "-Dgroups=explore"
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
    public void captureStructureOfPublicPages() throws IOException {
        ExplorerPage page = new ExplorerPage(getDriver());
        List<Path> reports = new ArrayList<>();

        for (SeedPage seed : SEED_PAGES) {
            page.visit(seed.path());
            reports.add(PageExplorer.explore(getDriver(), seed.name()));
        }

        // The first product's detail page, discovered from the products page
        page.visit("/products");
        page.visit(page.firstProductDetailHref());
        reports.add(PageExplorer.explore(getDriver(), "product_details_first"));

        for (Path report : reports) {
            assertTrue(Files.size(report) > 0, "Empty report: " + report);
        }
    }

    private static final class ExplorerPage extends BasePage {

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
        }

        String firstProductDetailHref() {
            List<WebElement> links = findAll(By.cssSelector("a[href*='/product_details/']"));
            if (links.isEmpty()) {
                throw new IllegalStateException("No product detail link found on the products page");
            }
            return links.get(0).getDomAttribute("href");
        }
    }
}