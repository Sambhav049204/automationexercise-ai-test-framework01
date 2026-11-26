package com.automationexercise.tests.framework;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.expectThrows;

import com.automationexercise.framework.base.BaseTest;
import com.automationexercise.framework.utils.XPaths;
import org.testng.annotations.Test;

public class XPathsTest extends BaseTest {

    private static final String HTML = """
            <html><body>
              <h2 style="text-transform: uppercase">Category</h2>
              <p>You Searched Products here</p>
            </body></html>
            """;

    @Test
    public void tagWithTextShouldIgnoreLetterCase() {
        getDriver().get(TestPages.dataUrl(HTML));

        assertEquals(getDriver().findElements(XPaths.tagWithText("h2", "CATEGORY")).size(), 1);
        assertEquals(getDriver().findElements(XPaths.tagWithText("h2", "category")).size(), 1);
        assertEquals(getDriver().findElements(XPaths.tagWithText("h2", "Brands")).size(), 0);
    }

    @Test
    public void containingTextShouldIgnoreLetterCaseAndRejectApostrophes() {
        getDriver().get(TestPages.dataUrl(HTML));

        assertEquals(getDriver().findElements(XPaths.containingText("searched products")).size(), 1);
        assertEquals(getDriver().findElements(XPaths.containingText("no such text")).size(), 0);
        expectThrows(IllegalArgumentException.class, () -> XPaths.containingText("it's"));
    }

        @Test
    public void containingTextShouldIgnoreThePageTitle() {
        getDriver().get(TestPages.dataUrl(
                "<html><head><title>Account Created</title></head><body><p>Hello</p></body></html>"));

        assertEquals(getDriver().findElements(XPaths.containingText("account created")).size(), 0);
    }
}