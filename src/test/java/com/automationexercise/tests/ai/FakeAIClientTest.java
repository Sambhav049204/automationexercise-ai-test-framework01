package com.automationexercise.tests.ai;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

import com.automationexercise.framework.ai.client.AiException;
import com.automationexercise.framework.ai.client.FakeAIClient;
import com.automationexercise.framework.ai.model.AiRequest;
import org.testng.annotations.Test;

public class FakeAIClientTest {

    @Test
    public void shouldReturnScriptedResponsesInOrder() {
        FakeAIClient client = new FakeAIClient().enqueueText("first").enqueueText("second");
        AiRequest request = AiRequest.simple("system prompt", "user prompt");

        assertEquals(client.complete(request).text(), "first");
        assertEquals(client.complete(request).text(), "second");
        assertEquals(client.remainingResponses(), 0);
    }

    @Test
    public void shouldRecordEveryRequest() {
        FakeAIClient client = new FakeAIClient().enqueueText("ok");

        client.complete(AiRequest.simple("be brief", "hello"));

        assertEquals(client.requests().size(), 1);
        assertEquals(client.requests().get(0).messages().get(1).content(), "hello");
    }

    @Test
    public void shouldFailLoudlyWhenNothingIsScripted() {
        FakeAIClient client = new FakeAIClient();

        AiException error = expectThrows(AiException.class, () -> client.complete(AiRequest.simple("s", "u")));

        assertTrue(error.getMessage().contains("no scripted response"), error.getMessage());
        assertFalse(error.isRetryable());
    }
}
