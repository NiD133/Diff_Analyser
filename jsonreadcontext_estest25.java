package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonReadContext} class, focusing on its name-handling capabilities.
 */
public class JsonReadContextTest {

    /**
     * Verifies that after setting a current name on a JsonReadContext,
     * both hasCurrentName() and getCurrentName() report the correct state.
     */
    @Test
    public void setCurrentName_shouldUpdateCurrentNameAndState() throws JsonProcessingException {
        // Arrange: Create a root context. The DupDetector is required but its behavior
        // is not the focus of this test.
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext context = JsonReadContext.createRootContext(dupDetector);
        String expectedFieldName = "testField";

        // Assert initial state (optional but good practice)
        assertFalse("Context should not have a current name initially", context.hasCurrentName());
        assertNull("Current name should be null initially", context.getCurrentName());

        // Act: Set the current name on the context.
        context.setCurrentName(expectedFieldName);

        // Assert: Verify that the context now has the correct current name.
        assertTrue("hasCurrentName() should return true after a name is set", context.hasCurrentName());
        assertEquals("getCurrentName() should return the name that was set", expectedFieldName, context.getCurrentName());
    }
}