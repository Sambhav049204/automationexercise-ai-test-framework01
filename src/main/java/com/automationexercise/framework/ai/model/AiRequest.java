package com.automationexercise.framework.ai.model;

import java.util.List;

/**
 * Everything sent to a model in one call: the conversation so far, the tools it may call and,
 * optionally, the JSON schema its answer must follow. The model name and limits come from configuration.
 */
public record AiRequest(List<AiMessage> messages, List<ToolSpec> tools, OutputSchema outputSchema) {

    public AiRequest {
        if (messages == null || messages.isEmpty()) {
            throw new IllegalArgumentException("A request needs at least one message");
        }
        messages = List.copyOf(messages);
        tools = tools == null ? List.of() : List.copyOf(tools);
    }

    /** A plain request: a system prompt, a user prompt, no tools and no schema. */
    public static AiRequest simple(String systemPrompt, String userPrompt) {
        return new AiRequest(List.of(AiMessage.system(systemPrompt), AiMessage.user(userPrompt)), List.of(), null);
    }
}
