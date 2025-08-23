package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link JsonReadContext} class.
 * This is an improved version of the original auto-generated test.
 */
// The original class name "JsonReadContext_ESTestTest12" and its base class
// are kept for context, but in a real-world scenario, they would be renamed
// to "JsonReadContextTest" and would not use test scaffolding if not needed.
public class JsonReadContext_ESTestTest12 extends JsonReadContext_ESTest_scaffolding {

    /**
     * Verifies that hasCurrentName() returns true after a name has been set on the context.
     */
    @Test
    public void hasCurrentName_shouldReturnTrue_whenNameIsSet() throws JsonProcessingException {
        // Arrange: Create a root-level JsonReadContext.
        JsonReadContext context = JsonReadContext.createRootContext(null);
        String fieldName = "testField";

        // Act: Set the current name on the context.
        context.setCurrentName(fieldName);

        // Assert: Verify that the context now reports having a current name.
        assertTrue("hasCurrentName() should return true after a name has been set.", context.hasCurrentName());
    }
}