package com.automationexercise.tests.ai;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

import com.automationexercise.framework.ai.model.AiMessage;
import com.automationexercise.framework.ai.model.AiRequest;
import com.automationexercise.framework.ai.model.Role;
import com.automationexercise.framework.ai.model.ToolSpec;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.util.List;
import org.testng.annotations.Test;

public class AiRequestTest {

    @Test
    public void shouldRequireAtLeastOneMessage() {
        expectThrows(IllegalArgumentException.class, () -> new AiRequest(List.of(), List.of(), null));
    }

    @Test
    public void shouldBuildSimpleRequestWithSystemAndUserMessages() {
        AiRequest request = AiRequest.simple("system text", "user text");

        assertEquals(request.messages().size(), 2);
        assertEquals(request.messages().get(0).role(), Role.SYSTEM);
        assertEquals(request.messages().get(1).role(), Role.USER);
        assertTrue(request.tools().isEmpty());
    }

    @Test
    public void shouldRejectInvalidToolNamesAndToolResultsWithoutCallId() {
        expectThrows(IllegalArgumentException.class,
                () -> new ToolSpec("bad name!", "d", JsonNodeFactory.instance.objectNode()));
        expectThrows(IllegalArgumentException.class, () -> AiMessage.toolResult(" ", "result"));
    }
}
