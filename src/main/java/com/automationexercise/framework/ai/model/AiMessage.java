package com.automationexercise.framework.ai.model;

import java.util.List;
import java.util.Objects;

/** One message in a conversation with a model. */
public record AiMessage(Role role, String content, String toolCallId, List<ToolCall> toolCalls) {

    public AiMessage {
        Objects.requireNonNull(role, "role");
        content = content == null ? "" : content;
        toolCalls = toolCalls == null ? List.of() : List.copyOf(toolCalls);
        if (role == Role.TOOL && (toolCallId == null || toolCallId.isBlank())) {
            throw new IllegalArgumentException("A tool result message needs the id of the tool call it answers");
        }
    }

    public static AiMessage system(String content) {
        return new AiMessage(Role.SYSTEM, content, null, List.of());
    }

    public static AiMessage user(String content) {
        return new AiMessage(Role.USER, content, null, List.of());
    }

    public static AiMessage assistant(String content) {
        return new AiMessage(Role.ASSISTANT, content, null, List.of());
    }

    /** An assistant turn in which the model asked for tools to be run. */
    public static AiMessage assistantToolCalls(List<ToolCall> calls) {
        return new AiMessage(Role.ASSISTANT, "", null, calls);
    }

    /** The result of running a tool, sent back to the model. */
    public static AiMessage toolResult(String toolCallId, String content) {
        return new AiMessage(Role.TOOL, content, toolCallId, List.of());
    }
}
