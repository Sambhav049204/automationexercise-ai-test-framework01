package com.automationexercise.framework.ai.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Objects;
import java.util.regex.Pattern;

/** The JSON schema an agent's final answer must follow (structured output). */
public record OutputSchema(String name, JsonNode schema) {

    private static final Pattern VALID_NAME = Pattern.compile("[A-Za-z0-9_-]{1,64}");

    public OutputSchema {
        if (name == null || !VALID_NAME.matcher(name).matches()) {
            throw new IllegalArgumentException(
                    "Schema names may contain only letters, digits, underscores and dashes (1 to 64 characters): " + name);
        }
        Objects.requireNonNull(schema, "schema");
    }
}
