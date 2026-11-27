package com.automationexercise.framework.ai.client;

import com.automationexercise.framework.ai.model.AiRequest;
import com.automationexercise.framework.ai.model.AiResponse;

/**
 * The only way the framework talks to a model. Agents and skills depend on this interface,
 * never on a provider, so models and providers can change without touching them.
 */
public interface AIClient {

    /** Sends one request and returns the model's answer. Throws {@link AiException} on failure. */
    AiResponse complete(AiRequest request);
}
