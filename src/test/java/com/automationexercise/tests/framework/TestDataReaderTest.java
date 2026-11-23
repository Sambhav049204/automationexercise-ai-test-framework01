package com.automationexercise.tests.framework;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

import com.automationexercise.framework.model.Product;
import com.automationexercise.framework.utils.TestDataReader;
import java.util.List;
import org.testng.annotations.Test;

public class TestDataReaderTest {

    @Test
    public void shouldReadListOfProducts() {
        List<Product> products = TestDataReader.readList("products.json", Product.class);

        assertEquals(products.size(), 3);
    }

    @Test
    public void shouldMapJsonFieldsToRecord() {
        Product first = TestDataReader.readList("products.json", Product.class).get(0);

        assertEquals(first, new Product(1, "Blue Top", 500));
    }

    @Test
    public void shouldReadArrayAsSingleType() {
        Product[] products = TestDataReader.read("products.json", Product[].class);

        assertEquals(products.length, 3);
        assertEquals(products[2].name(), "Sleeveless Dress");
    }

    @Test
    public void shouldNameMissingFileInErrorMessage() {
        IllegalArgumentException error = expectThrows(IllegalArgumentException.class,
                () -> TestDataReader.readList("does-not-exist.json", Product.class));

        assertTrue(error.getMessage().contains("does-not-exist.json"), error.getMessage());
    }

    @Test
    public void shouldReportMalformedJsonWithFileName() {
        IllegalStateException error = expectThrows(IllegalStateException.class,
                () -> TestDataReader.readList("_invalid-sample.json", Product.class));

        assertTrue(error.getMessage().contains("_invalid-sample.json"), error.getMessage());
    }

    @Test
    public void shouldRejectPathTraversal() {
        expectThrows(IllegalArgumentException.class,
                () -> TestDataReader.read("../config/config.properties", String.class));
    }
}