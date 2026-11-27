package com.automationexercise.framework.ai.client;

import com.automationexercise.framework.ai.model.AiRequest;
import com.automationexercise.framework.ai.model.AiResponse;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

/**
 * A scripted stand-in for a real model. Answers come from a queue you fill in advance, and every request
 * is recorded. Tests of agents and skills use it, so they run offline, free and deterministic.
 */
public class FakeAIClient implements AIClient {

    private final Deque<AiResponse> scripted = new ArrayDeque<>();
    private final List<AiRequest> received = new ArrayList<>();

    public synchronized FakeAIClient enqueue(AiResponse response) {
        scripted.addLast(Objects.requireNonNull(response, "response"));
        return this;
    }

    public FakeAIClient enqueueText(String text) {
        return enqueue(AiResponse.ofText(text));
    }

    @Override
    public synchronized AiResponse complete(AiRequest request) {
        received.add(Objects.requireNonNull(request, "request"));
        AiResponse next = scripted.pollFirst();
        if (next == null) {
            throw new AiException("FakeAIClient has no scripted response left (request number "
                    + received.size() + ")", false, null);
        }
        return next;
    }

    /** Every request received so far, oldest first. */
    public synchronized List<AiRequest> requests() {
        return List.copyOf(received);
    }

    public synchronized int remainingResponses() {
        return scripted.size();
    }
}
