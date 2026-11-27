package com.automationexercise.framework.ai.model;

import java.util.List;

/** What a model sent back: text, tool calls it wants run, or both, plus token usage. */
public record AiResponse(String text, List<ToolCall> toolCalls, Usage usage) {

    public AiResponse {
        text = text == null ? "" : text;
        toolCalls = toolCalls == null ? List.of() : List.copyOf(toolCalls);
        usage = usage == null ? Usage.NONE : usage;
    }

    public static AiResponse ofText(String text) {
        return new AiResponse(text, List.of(), Usage.NONE);
    }

    public static AiResponse ofToolCalls(List<ToolCall> calls) {
        return new AiResponse("", calls, Usage.NONE);
    }

    public boolean hasToolCalls() {
        return !toolCalls.isEmpty();
    }
}
