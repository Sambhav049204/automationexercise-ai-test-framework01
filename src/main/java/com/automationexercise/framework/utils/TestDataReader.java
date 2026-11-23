package com.automationexercise.framework.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Reads JSON test data from {@code src/test/resources/testdata/} into typed objects.
 * Parsing is strict: unknown fields cause an error, so typos in data files cannot hide.
 */
public final class TestDataReader {

    private static final String BASE_PATH = "testdata/";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private TestDataReader() {
        // utility class
    }

    /** Reads one JSON value (an object, or an array when {@code type} is an array type). */
    public static <T> T read(String fileName, Class<T> type) {
        try (InputStream in = open(fileName)) {
            return MAPPER.readValue(in, type);
        } catch (IOException e) {
            throw parseFailure(fileName, e);
        }
    }

    /** Reads a JSON array into a list of typed objects. */
    public static <T> List<T> readList(String fileName, Class<T> elementType) {
        try (InputStream in = open(fileName)) {
            JavaType listType = MAPPER.getTypeFactory().constructCollectionType(List.class, elementType);
            return MAPPER.readValue(in, listType);
        } catch (IOException e) {
            throw parseFailure(fileName, e);
        }
    }

    private static InputStream open(String fileName) {
        if (fileName == null || fileName.isBlank() || fileName.contains("..")) {
            throw new IllegalArgumentException("Invalid test data file name: " + fileName);
        }
        InputStream in = TestDataReader.class.getClassLoader().getResourceAsStream(BASE_PATH + fileName);
        if (in == null) {
            throw new IllegalArgumentException("Test data file not found on classpath: " + BASE_PATH + fileName);
        }
        return in;
    }

    private static IllegalStateException parseFailure(String fileName, IOException cause) {
        // getOriginalMessage() leaves out the source snippet, so file contents never reach logs
        String reason = cause instanceof JsonProcessingException json ? json.getOriginalMessage() : cause.getMessage();
        return new IllegalStateException("Could not read test data file '" + BASE_PATH + fileName + "': " + reason, cause);
    }
}