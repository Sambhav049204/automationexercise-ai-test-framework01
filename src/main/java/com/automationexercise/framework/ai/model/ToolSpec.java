package com.automationexercise.framework.ai.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Objects;
import java.util.regex.Pattern;

/** Describes a tool (skill) the model may call: its name, purpose and JSON schema of its arguments. */
public record ToolSpec(String name, String description, JsonNode parametersSchema) {

    private static final Pattern VALID_NAME = Pattern.compile("[A-Za-z0-9_-]{1,64}");

    public ToolSpec {
        if (name == null || !VALID_NAME.matcher(name).matches()) {
            throw new IllegalArgumentException(
                    "Tool names may contain only letters, digits, underscores and dashes (1 to 64 characters): " + name);
        }
        description = description == null ? "" : description;
        Objects.requireNonNull(parametersSchema, "parametersSchema");
    }
}
