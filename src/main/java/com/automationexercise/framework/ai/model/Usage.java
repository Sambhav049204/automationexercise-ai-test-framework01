package com.automationexercise.framework.ai.model;

/** Token counts reported by the model for one call. */
public record Usage(int inputTokens, int outputTokens) {

    public static final Usage NONE = new Usage(0, 0);

    public Usage {
        if (inputTokens < 0 || outputTokens < 0) {
            throw new IllegalArgumentException("Token counts cannot be negative");
        }
    }

    public int totalTokens() {
        return inputTokens + outputTokens;
    }

    public Usage plus(Usage other) {
        return new Usage(inputTokens + other.inputTokens, outputTokens + other.outputTokens);
    }
}
