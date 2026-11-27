package com.automationexercise.framework.ai.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/** A request from the model to run one of the offered tools (skills). */
public record ToolCall(String id, String name, JsonNode arguments) {

    public ToolCall {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("A tool call needs an id");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("A tool call needs a tool name");
        }
        arguments = arguments == null ? JsonNodeFactory.instance.objectNode() : arguments;
    }
}
