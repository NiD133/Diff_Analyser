package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.DupDetector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JUnit5TestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for the {@link JsonReadContext} class, focusing on its name-handling capabilities.
 */
class JsonReadContextTest extends JUnit5TestBase {

    @Test
    void shouldSetAndRetrieveCurrentName() throws JsonProcessingException {
        // Arrange: Create a root context. The duplicate detector is null, as it's not
        // relevant to this test.
        JsonReadContext context = JsonReadContext.createRootContext(null);
        String expectedName = "abc";

        // Act: Set the current name on the context.
        context.setCurrentName(expectedName);

        // Assert: Verify that getCurrentName() returns the name that was set.
        assertEquals(expectedName, context.getCurrentName());
    }

    @Test
    void shouldAllowSettingCurrentNameToNull() throws JsonProcessingException {
        // Arrange: Create a context and give it an initial name to ensure we are testing a state change.
        JsonReadContext context = JsonReadContext.createRootContext(null);
        context.setCurrentName("initialName");

        // Act: Set the current name to null.
        context.setCurrentName(null);

        // Assert: Verify that the current name is now null.
        assertNull(context.getCurrentName());
    }
}