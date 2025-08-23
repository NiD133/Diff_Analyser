package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link JsonReadContext} class, focusing on its behavior
 * with duplicate field names.
 */
public class JsonReadContextTest {

    /**
     * Verifies that calling {@link JsonReadContext#setCurrentName(String)} twice
     * with the same name throws a {@link JsonProcessingException} when duplicate
     * detection is enabled.
     */
    @Test
    public void setCurrentName_whenCalledWithDuplicateName_shouldThrowException() throws JsonProcessingException {
        // Arrange: Create a root context with duplicate detection enabled.
        JsonFactory factory = new JsonFactory();
        // A parser instance is required to create a root DupDetector.
        JsonParser parser = factory.createNonBlockingByteBufferParser();
        DupDetector dupDetector = DupDetector.rootDetector(parser);
        JsonReadContext context = JsonReadContext.createRootContext(dupDetector);
        final String fieldName = "JSON";

        // Set the field name for the first time (this should succeed).
        context.setCurrentName(fieldName);

        // Act & Assert: Attempt to set the same field name again and verify the exception.
        try {
            context.setCurrentName(fieldName);
            fail("Expected a JsonProcessingException to be thrown for a duplicate field name.");
        } catch (JsonProcessingException e) {
            // Assert that the exception message clearly indicates a duplicate field.
            String expectedMessageFragment = "Duplicate field '" + fieldName + "'";
            assertTrue(
                "Exception message should contain '" + expectedMessageFragment + "', but was: " + e.getMessage(),
                e.getMessage().contains(expectedMessageFragment)
            );
        }
    }
}