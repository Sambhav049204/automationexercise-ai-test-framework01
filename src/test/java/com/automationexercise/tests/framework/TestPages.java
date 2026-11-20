package com.automationexercise.tests.framework;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/** Turns inline HTML into a data: URL so framework tests need no web server or internet. */
final class TestPages {

    private TestPages() {
    }

    static String dataUrl(String html) {
        return "data:text/html;charset=utf-8,"
                + URLEncoder.encode(html, StandardCharsets.UTF_8).replace("+", "%20");
    }
}