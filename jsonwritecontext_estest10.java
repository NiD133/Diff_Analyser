package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link JsonWriteContext} class, focusing on name-related state.
 */
public class JsonWriteContextTest {

    /**
     * Verifies that hasCurrentName() returns true after a field name has been set
     * using the public API.
     */
    @Test
    public void hasCurrentName_shouldReturnTrue_whenNameIsSet() throws JsonProcessingException {
        // Arrange: Create an object context, which is the natural place for a field name.
        JsonWriteContext context = JsonWriteContext.createRootContext()
                                                   .createChildObjectContext();

        // Act: Set the current field name using the public writeFieldName() method.
        context.writeFieldName("testField");
        boolean hasName = context.hasCurrentName();

        // Assert: The context should now report that it has a current name.
        assertTrue("hasCurrentName() should return true after a field name is written", hasName);
    }
}