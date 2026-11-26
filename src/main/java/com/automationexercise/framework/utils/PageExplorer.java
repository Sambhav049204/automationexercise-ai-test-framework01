package com.automationexercise.framework.utils;

import com.automationexercise.framework.config.ConfigReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
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
 * Records what a page really contains (controls, headings, links, text, iframes) so locators can be
 * written from evidence. Read-only: it never clicks, types or submits anything.
 */
public final class PageExplorer {

    private static final Logger LOG = LogManager.getLogger(PageExplorer.class);

    private static final Set<String> CONTROL_TAGS = Set.of("input", "select", "textarea", "button", "form");
    private static final Set<String> HEADING_TAGS = Set.of("h1", "h2", "h3", "h4");

    private static final String HELPERS = """
            function describe(node) {
              var cls = (typeof node.className === 'string' ? node.className : '').trim().replace(/\\s+/g, '.');
              var label = node.tagName.toLowerCase() + (node.id ? '#' + node.id : '') + (cls ? '.' + cls : '');
              return label.substring(0, 40);
            }
            function parents(e) {
              var out = [];
              var p = e.parentElement;
              for (var i = 0; i < 3 && p; i++) { out.push(describe(p)); p = p.parentElement; }
              return out.join(' < ').substring(0, 110);
            }
            function isVisible(e) {
              var rect = e.getBoundingClientRect();
              var style = window.getComputedStyle(e);
              return (rect.width > 0 || rect.height > 0) && style.visibility !== 'hidden' && style.display !== 'none';
            }
            function dataAttrs(e) {
              return Array.from(e.attributes)
                .filter(function (a) { return a.name.indexOf('data-') === 0 && a.name !== 'data-qa'; })
                .map(function (a) { return a.name + '=' + a.value.substring(0, 30); })
                .join(' ');
            }
            """;

    private static final String ELEMENTS_SCRIPT = HELPERS + """
            const selector = 'a, button, input, select, textarea, form, h1, h2, h3, h4, [data-qa]';
            return Array.from(document.querySelectorAll(selector)).slice(0, 600).map(function (e) {
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
                visible: isVisible(e),
                data: dataAttrs(e),
                parents: parents(e)
              };
            });
            """;

    private static final String TEXT_SCRIPT = HELPERS + """
            const selector = 'p, label, td, th, li, span, b, strong, small';
            return Array.from(document.querySelectorAll(selector)).filter(function (e) {
              const text = (e.innerText || '').trim();
              return e.children.length === 0 && text.length > 0 && text.length <= 60 && isVisible(e);
            }).slice(0, 400).map(function (e) {
              const cls = (typeof e.className === 'string' ? e.className : '').trim().replace(/\\s+/g, ' ');
              return {
                tag: e.tagName.toLowerCase(),
                id: e.id || '',
                cls: cls.substring(0, 60),
                text: (e.innerText || '').trim().replace(/\\s+/g, ' '),
                parents: parents(e),
                visible: true
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
        List<Map<String, Object>> textBlocks = (List<Map<String, Object>>) js.executeScript(TEXT_SCRIPT);
        List<String> iframeHosts = (List<String>) js.executeScript(IFRAMES_SCRIPT);

        StringBuilder report = new StringBuilder();
        report.append("PAGE: ").append(name).append('\n');
        report.append("URL: ").append(driver.getCurrentUrl()).append('\n');
        report.append("TITLE: ").append(driver.getTitle()).append('\n');
        report.append("IFRAMES: ").append(iframeHosts.isEmpty() ? "none" : String.join(", ", iframeHosts)).append('\n');
        report.append("NOTE: identical rows are merged; the last column xN is how many there were\n\n");

        appendSection(report, "FORMS AND CONTROLS", elements, CONTROL_TAGS::contains);
        appendSection(report, "HEADINGS", elements, HEADING_TAGS::contains);
        appendSection(report, "LINKS", elements, "a"::equals);
        appendSection(report, "OTHER ELEMENTS WITH data-qa", elements,
                tag -> !CONTROL_TAGS.contains(tag) && !HEADING_TAGS.contains(tag) && !"a".equals(tag));
        appendSection(report, "TEXT BLOCKS (visible leaf text)", textBlocks, tag -> true);

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
        LOG.info("Explored '{}' ({} elements, {} text blocks) -> {}", name, elements.size(), textBlocks.size(), reportFile);
        return reportFile;
    }

    private static void appendSection(StringBuilder report, String title,
                                      List<Map<String, Object>> elements, Predicate<String> tagFilter) {
        report.append("== ").append(title).append(" ==\n");
        report.append("tag | id | name | type | data-qa | class | text | href | placeholder | visible | data | parents | count\n");

        Map<String, Integer> counts = new LinkedHashMap<>();
        Map<String, String> firstRows = new LinkedHashMap<>();
        for (Map<String, Object> e : elements) {
            if (!tagFilter.test(value(e, "tag"))) {
                continue;
            }
            // href and data differ per product, so they are left out of the merge key
            String key = String.join(" | ", value(e, "tag"), value(e, "id"), value(e, "name"), value(e, "type"),
                    value(e, "qa"), value(e, "cls"), value(e, "text"), value(e, "placeholder"),
                    value(e, "visible"), value(e, "parents"));
            String row = String.join(" | ", value(e, "tag"), value(e, "id"), value(e, "name"), value(e, "type"),
                    value(e, "qa"), value(e, "cls"), value(e, "text"), value(e, "href"),
                    value(e, "placeholder"), value(e, "visible"), value(e, "data"), value(e, "parents"));
            counts.merge(key, 1, Integer::sum);
            firstRows.putIfAbsent(key, row);
        }
        for (Map.Entry<String, String> entry : firstRows.entrySet()) {
            report.append(entry.getValue()).append(" | x").append(counts.get(entry.getKey())).append('\n');
        }
        report.append('\n');
    }

    private static String value(Map<String, Object> element, String key) {
        Object value = element.get(key);
        return value == null ? "" : value.toString();
    }
}