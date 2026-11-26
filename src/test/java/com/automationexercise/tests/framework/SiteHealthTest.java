package com.automationexercise.tests.framework;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import com.automationexercise.framework.base.BaseTest;
import com.automationexercise.framework.utils.SiteHealth;
import org.testng.annotations.Test;

public class SiteHealthTest extends BaseTest {

    @Test
    public void shouldRecognizeTheOverloadPage() {
        getDriver().get(TestPages.dataUrl(
                "<html><head></head><body><h2>This website is under heavy load (queue full)</h2></body></html>"));

        assertTrue(SiteHealth.isOverloaded(getDriver()));
    }

    @Test
    public void shouldNotMistakeNormalPagesForOverload() {
        getDriver().get(TestPages.dataUrl(
                "<html><head><title>Sample</title></head><body><p>queue full of messages</p></body></html>"));
        assertFalse(SiteHealth.isOverloaded(getDriver()));

        getDriver().get(TestPages.dataUrl("<html><head></head><body><p>Blank title only</p></body></html>"));
        assertFalse(SiteHealth.isOverloaded(getDriver()));
    }
}