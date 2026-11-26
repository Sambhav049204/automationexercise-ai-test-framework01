package com.automationexercise.tests.regression.products;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import com.automationexercise.framework.base.BaseTest;
import com.automationexercise.framework.pages.ProductsPage;
import org.testng.annotations.Test;

public class ProductListingTest extends BaseTest {

    @Test(groups = "regression", description = "PRD-005 and BRD-004: product count equals the sum of the brand counts")
    public void productCountShouldMatchSumOfBrandCounts() {
        ProductsPage page = new ProductsPage(getDriver()).open();

        int products = page.productCount();

        assertTrue(products > 0, "No products are listed");
        assertEquals(products, page.brandProductTotal(), "Product count differs from the sum of the brand counts");
    }

    @Test(groups = "regression", description = "SRC-001: searching narrows the product list")
    public void searchShouldNarrowTheProductList() {
        ProductsPage page = new ProductsPage(getDriver()).open();
        int allProducts = page.productCount();

        page.searchFor("Top");

        assertTrue(page.hasSearchResultsHeading(), "SEARCHED PRODUCTS heading did not appear");
        int found = page.productCount();
        assertTrue(found > 0 && found < allProducts,
                "Expected between 1 and " + (allProducts - 1) + " results but found " + found);
    }
}