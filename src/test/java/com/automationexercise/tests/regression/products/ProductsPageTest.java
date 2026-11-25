package com.automationexercise.tests.regression.products;

import static org.testng.Assert.assertTrue;

import com.automationexercise.framework.base.BaseTest;
import com.automationexercise.framework.pages.ProductsPage;
import org.testng.annotations.Test;

public class ProductsPageTest extends BaseTest {

    @Test(groups = "regression", description = "PRD-001: the products page shows the ALL PRODUCTS heading")
    public void shouldShowAllProductsHeading() {
        ProductsPage page = new ProductsPage(getDriver()).open();

        String heading = page.headingText();
        assertTrue("ALL PRODUCTS".equalsIgnoreCase(heading), "Unexpected heading: " + heading);
    }

    @Test(groups = "regression", description = "PRD-007: the products page shows the Category and Brands sidebars")
    public void shouldShowCategoryAndBrandsSidebars() {
        ProductsPage page = new ProductsPage(getDriver()).open();

        assertTrue(page.sidebar().isCategoryHeadingVisible(), "Category heading is not visible");
        assertTrue(page.sidebar().isBrandsHeadingVisible(), "Brands heading is not visible");
    }

    @Test(groups = "regression", description = "SRC-001: searching shows the SEARCHED PRODUCTS heading")
    public void searchShouldShowSearchedProductsHeading() {
        ProductsPage page = new ProductsPage(getDriver()).open();

        page.searchFor("Top");

        assertTrue(page.hasSearchResultsHeading(), "SEARCHED PRODUCTS heading did not appear");
    }
}