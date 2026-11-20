package com.automationexercise.tests.framework;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import com.automationexercise.framework.base.BasePage;
import com.automationexercise.framework.driver.BrowserType;
import com.automationexercise.framework.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BasePageTest {

    private static final String HTML = """
            <html>
            <head><title>Sample Page</title></head>
            <body>
              <input id="name" type="text">
              <button id="go" onclick="document.getElementById('out').innerText = document.getElementById('name').value">Go</button>
              <div id="out"></div>
              <button id="load-late" onclick="setTimeout(function () { var d = document.createElement('div'); d.id = 'late'; d.innerText = 'arrived'; document.body.appendChild(d); }, 1000)">Load</button>
              <select id="color"><option>Red</option><option>Blue</option></select>
              <div id="hidden" style="display:none">secret</div>
            </body>
            </html>
            """;

    private SamplePage page;

    @BeforeMethod
    public void setUp() {
        DriverManager.initDriver(BrowserType.CHROME, true);
        page = new SamplePage(DriverManager.getDriver());
        page.load();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }

    @Test
    public void shouldTypeIntoFieldAndReadItBack() {
        page.enterName("Alice");
        assertEquals(page.nameValue(), "Alice");
    }

    @Test
    public void shouldClickButtonAndShowResult() {
        page.enterName("Bob");
        page.clickGo();
        assertEquals(page.outputText(), "Bob");
    }

    @Test
    public void shouldWaitForElementThatAppearsLater() {
        page.clickLoadLate();
        assertEquals(page.lateText(), "arrived");
    }

    @Test
    public void shouldSelectOptionByVisibleText() {
        page.chooseColor("Blue");
        assertEquals(page.selectedColor(), "Blue");
    }

    @Test
    public void shouldReportDisplayedStateWithoutThrowing() {
        assertTrue(page.isNameFieldVisible());
        assertFalse(page.isHiddenElementVisible());
        assertFalse(page.isMissingElementVisible());
    }

    @Test
    public void shouldExposeTitleOfCurrentPage() {
        assertEquals(page.getTitle(), "Sample Page");
    }

    /** Minimal page object that exercises the protected BasePage actions. */
    private static final class SamplePage extends BasePage {

        private static final By NAME = By.id("name");
        private static final By GO = By.id("go");
        private static final By OUT = By.id("out");
        private static final By LOAD_LATE = By.id("load-late");
        private static final By LATE = By.id("late");
        private static final By COLOR = By.id("color");
        private static final By HIDDEN = By.id("hidden");
        private static final By MISSING = By.id("does-not-exist");

        SamplePage(WebDriver driver) {
            super(driver);
        }

        void load() {
            open(TestPages.dataUrl(HTML));
        }

        void enterName(String value) {
            type(NAME, value);
        }

        String nameValue() {
            return getDomProperty(NAME, "value");
        }

        void clickGo() {
            click(GO);
        }

        String outputText() {
            return getText(OUT);
        }

        void clickLoadLate() {
            click(LOAD_LATE);
        }

        String lateText() {
            return getText(LATE);
        }

        void chooseColor(String color) {
            selectByVisibleText(COLOR, color);
        }

        String selectedColor() {
            return getSelectedOptionText(COLOR);
        }

        boolean isNameFieldVisible() {
            return isDisplayed(NAME);
        }

        boolean isHiddenElementVisible() {
            return isDisplayed(HIDDEN);
        }

        boolean isMissingElementVisible() {
            return isDisplayed(MISSING);
        }
    }
}