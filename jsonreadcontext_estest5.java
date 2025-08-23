package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link JsonReadContext} class.
 */
public class JsonReadContextTest {

    /**
     * Verifies that hasCurrentName() returns true after a name has been set.
     * This test also ensures that the current name state is preserved even after
     * re-assigning a duplicate detector.
     */
    @Test
    public void hasCurrentName_shouldReturnTrue_whenNameIsSet() throws JsonProcessingException {
        // Arrange: Create a root context with a duplicate detector.
        DupDetector dupDetector = DupDetector.rootDetector((JsonParser) null);
        JsonReadContext context = JsonReadContext.createRootContext(1, 1, dupDetector);
        String fieldName = "testField";

        // Act: Set the current name on the context.
        context.setCurrentName(fieldName);
        
        // This call ensures that re-setting the detector doesn't clear the current name state.
        context.withDupDetector(dupDetector);

        // Assert: Verify that the context correctly reports it has a current name.
        assertTrue("Context should have a current name after it has been set.", context.hasCurrentName());
    }
}