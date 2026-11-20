package com.automationexercise.tests.framework;

import static org.testng.Assert.assertTrue;

import com.automationexercise.framework.driver.BrowserType;
import com.automationexercise.framework.driver.DriverManager;
import com.automationexercise.framework.utils.ScreenshotUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ScreenshotUtilsTest {

    private Path savedFile;

    @BeforeMethod
    public void setUp() {
        DriverManager.initDriver(BrowserType.CHROME, true);
        DriverManager.getDriver().get(TestPages.dataUrl("<html><body><h1>screenshot</h1></body></html>"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() throws IOException {
        DriverManager.quitDriver();
        if (savedFile != null) {
            Files.deleteIfExists(savedFile);
        }
    }

    @Test
    public void shouldSaveNonEmptyPngFile() throws IOException {
        savedFile = ScreenshotUtils.capture(DriverManager.getDriver(), "unit-test");

        assertTrue(Files.exists(savedFile), "Screenshot file missing: " + savedFile);
        byte[] bytes = Files.readAllBytes(savedFile);
        assertTrue(bytes.length > 100, "Screenshot is suspiciously small: " + bytes.length + " bytes");
        boolean pngSignature = bytes[0] == (byte) 0x89 && bytes[1] == 'P' && bytes[2] == 'N' && bytes[3] == 'G';
        assertTrue(pngSignature, "File is not a PNG image");
    }

    @Test
    public void shouldSanitizeUnsafeCharactersInFileName() {
        savedFile = ScreenshotUtils.capture(DriverManager.getDriver(), "My Test/Name:1");

        String fileName = savedFile.getFileName().toString();
        assertTrue(fileName.matches("[A-Za-z0-9._-]+"), "Unsafe characters in file name: " + fileName);
    }
}