package com.automationexercise.tests.regression.home;

import static org.testng.Assert.assertTrue;

import com.automationexercise.framework.base.BaseTest;
import com.automationexercise.framework.components.HeaderComponent;
import com.automationexercise.framework.components.NavLink;
import com.automationexercise.framework.pages.HomePage;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.Test;

public class NavigationTest extends BaseTest {

    @Test(groups = "regression", description = "HOM-002: every top navigation link is visible")
    public void allNavigationLinksShouldBeVisible() {
        HeaderComponent header = new HomePage(getDriver()).open().header();

        List<NavLink> missing = new ArrayList<>();
        for (NavLink link : NavLink.values()) {
            if (!header.isVisible(link)) {
                missing.add(link);
            }
        }

        assertTrue(missing.isEmpty(), "Navigation links not visible: " + missing);
    }

    @Test(groups = "regression", description = "HOM-003: each navigation link opens its page")
    public void eachNavigationLinkShouldOpenItsPage() {
        List<String> problems = new ArrayList<>();

        for (NavLink link : NavLink.values()) {
            new HomePage(getDriver()).open().header().open(link);

            String actualPath = URI.create(getDriver().getCurrentUrl()).getPath();
            if (!actualPath.equals(link.path())) {
                problems.add(link + " opened " + actualPath);
            }
        }

        assertTrue(problems.isEmpty(), "Wrong destination: " + problems);
    }
}