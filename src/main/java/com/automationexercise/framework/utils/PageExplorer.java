package com.automationexercise.framework.utils;

import com.automationexercise.framework.config.ConfigReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Records what a page really contains (controls, headings, links, iframes) so locators can be
 * written from evidence. Read-only: it never clicks, types or submits anything.
 */
public final class PageExplorer {

    private static final Logger LOG = LogManager.getLogger(PageExplorer.class);

    private static final Set<String> CONTROL_TAGS = Set.of("input", "select", "textarea", "button", "form");
    private static final Set<String> HEADING_TAGS = Set.of("h1", "h2", "h3", "h4");

    private static final String ELEMENTS_SCRIPT = """
            const selector = 'a[href], button, input, select, textarea, form, h1, h2, h3, h4, [data-qa]';
            return Array.from(document.querySelectorAll(selector)).slice(0, 400).map(function (e) {
              const rect = e.getBoundingClientRect();
              const style = window.getComputedStyle(e);
              const visible = (rect.width > 0 || rect.height > 0)
                  && style.visibility !== 'hidden' && style.display !== 'none';
              const cls = (typeof e.className === 'string' ? e.className : '').trim().replace(/\\s+/g, ' ');
              return {
                tag: e.tagName.toLowerCase(),
                id: e.id || '',
                name: e.getAttribute('name') || '',
                type: e.getAttribute('type') || '',
                qa: e.getAttribute('data-qa') || '',
                cls: cls.substring(0, 60),
                text: (e.innerText || '').trim().replace(/\\s+/g, ' ').substring(0, 50),
                href: e.getAttribute('href') || '',
                placeholder: e.getAttribute('placeholder') || '',
                visible: visible
              };
            });
            """;

    private static final String IFRAMES_SCRIPT = """
            return Array.from(document.querySelectorAll('iframe')).map(function (f) {
              try { return new URL(f.src, location.href).host || 'inline'; } catch (e) { return 'unknown'; }
            });
            """;

    private PageExplorer() {
        // utility class
    }

    /** Saves {@code <name>.txt}, {@code .png} and {@code .html} and returns the path of the .txt report. */
    @SuppressWarnings("unchecked")
    public static Path explore(WebDriver driver, String name) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        List<Map<String, Object>> elements = (List<Map<String, Object>>) js.executeScript(ELEMENTS_SCRIPT);
        List<String> iframeHosts = (List<String>) js.executeScript(IFRAMES_SCRIPT);

        StringBuilder report = new StringBuilder();
        report.append("PAGE: ").append(name).append('\n');
        report.append("URL: ").append(driver.getCurrentUrl()).append('\n');
        report.append("TITLE: ").append(driver.getTitle()).append('\n');
        report.append("IFRAMES: ").append(iframeHosts.isEmpty() ? "none" : String.join(", ", iframeHosts)).append("\n\n");

        appendSection(report, "FORMS AND CONTROLS", elements, CONTROL_TAGS::contains);
        appendSection(report, "HEADINGS", elements, HEADING_TAGS::contains);
        appendSection(report, "LINKS", elements, "a"::equals);
        appendSection(report, "OTHER ELEMENTS WITH data-qa", elements,
                tag -> !CONTROL_TAGS.contains(tag) && !HEADING_TAGS.contains(tag) && !"a".equals(tag));

        String safeName = name.replaceAll("[^A-Za-z0-9._-]", "_");
        Path directory = Path.of(ConfigReader.get("exploration.dir", "exploration"));
        Path reportFile = directory.resolve(safeName + ".txt");
        try {
            Files.createDirectories(directory);
            Files.writeString(reportFile, report.toString());
            Files.writeString(directory.resolve(safeName + ".html"), driver.getPageSource());
            Files.write(directory.resolve(safeName + ".png"),
                    ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
        } catch (IOException e) {
            throw new UncheckedIOException("Could not save exploration files for " + name, e);
        }
        LOG.info("Explored '{}' ({} elements) -> {}", name, elements.size(), reportFile);
        return reportFile;
    }

    private static void appendSection(StringBuilder report, String title,
                                      List<Map<String, Object>> elements, Predicate<String> tagFilter) {
        report.append("== ").append(title).append(" ==\n");
        report.append("tag | id | name | type | data-qa | class | text | href | placeholder | visible\n");
        for (Map<String, Object> element : elements) {
            if (tagFilter.test(value(element, "tag"))) {
                report.append(String.join(" | ",
                        value(element, "tag"), value(element, "id"), value(element, "name"),
                        value(element, "type"), value(element, "qa"), value(element, "cls"),
                        value(element, "text"), value(element, "href"), value(element, "placeholder"),
                        value(element, "visible"))).append('\n');
            }
        }
        report.append('\n');
    }

    private static String value(Map<String, Object> element, String key) {
        Object value = element.get(key);
        return value == null ? "" : value.toString();
    }
}