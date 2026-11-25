package com.automationexercise.tests.regression.products;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import com.automationexercise.framework.base.BaseTest;
import com.automationexercise.framework.components.Category;
import com.automationexercise.framework.components.CategoryGroup;
import com.automationexercise.framework.components.SidebarComponent;
import com.automationexercise.framework.pages.ProductsPage;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.Test;

public class CategoryAndBrandTest extends BaseTest {

    // Brands observed in the sidebar of the product detail page report
    private static final List<String> BRANDS = List.of(
            "Polo", "H&M", "Madame", "Mast & Harbour", "Babyhug", "Allen Solly Junior", "Kookie Kids");

    @Test(groups = "regression", description = "CAT-002: expanding Women shows Dress, Tops and Saree")
    public void expandingWomenShouldShowItsSubcategories() {
        SidebarComponent sidebar = new ProductsPage(getDriver()).open().sidebar();

        sidebar.expand(CategoryGroup.WOMEN);

        List<Category> missing = new ArrayList<>();
        for (Category category : Category.values()) {
            if (category.group() == CategoryGroup.WOMEN && !sidebar.waitUntilVisible(category)) {
                missing.add(category);
            }
        }
        assertTrue(missing.isEmpty(), "Sub-categories not visible after expanding Women: " + missing);
    }

    @Test(groups = "regression", description = "CAT-002 to CAT-004: every sub-category link opens its category page")
    public void eachCategoryLinkShouldOpenItsPage() {
        List<String> problems = new ArrayList<>();

        for (Category category : Category.values()) {
            new ProductsPage(getDriver()).open().sidebar().openCategory(category);

            String actualPath = URI.create(getDriver().getCurrentUrl()).getPath();
            if (!actualPath.equals(category.path())) {
                problems.add(category + " opened " + actualPath);
            }
        }

        assertTrue(problems.isEmpty(), "Wrong destination: " + problems);
    }

    @Test(groups = "regression", description = "BRD-001: the observed brands are listed in the sidebar")
    public void brandsShouldBeListed() {
        SidebarComponent sidebar = new ProductsPage(getDriver()).open().sidebar();

        List<String> missing = new ArrayList<>();
        for (String brand : BRANDS) {
            if (!sidebar.isBrandVisible(brand)) {
                missing.add(brand);
            }
        }
        assertTrue(missing.isEmpty(), "Brands not visible: " + missing);
    }

    @Test(groups = "regression", description = "BRD-002: clicking a brand opens its page")
    public void brandLinkShouldOpenBrandPage() {
        new ProductsPage(getDriver()).open().sidebar().openBrand("Polo");

        assertEquals(URI.create(getDriver().getCurrentUrl()).getPath(), "/brand_products/Polo");
    }
}