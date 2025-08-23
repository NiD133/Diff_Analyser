package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonParser;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link JsonReadContext} class, focusing on state management and error handling.
 */
public class JsonReadContextTest {

    /**
     * Verifies that calling {@link JsonReadContext#setCurrentName(String)} with the same field name
     * twice within the same object context triggers a duplicate field exception.
     */
    @Test
    public void setCurrentName_whenSettingDuplicateFieldName_shouldThrowIOException() throws IOException {
        // Arrange: Set up a context that simulates parsing an object and can detect duplicates.
        final String fieldName = "user";
        DupDetector dupDetector = DupDetector.rootDetector((JsonParser) null);
        // Duplicate fields are relevant within an object, so we create a child object context.
        JsonReadContext objectContext = JsonReadContext.createRootContext(dupDetector)
                                                       .createChildObjectContext(1, 1);

        // Act & Assert

        // The first occurrence of the field name should be processed without error.
        objectContext.setCurrentName(fieldName);

        // The second occurrence of the same field name should be detected as a duplicate.
        try {
            objectContext.setCurrentName(fieldName);
            fail("Expected an IOException because the field name is a duplicate.");
        } catch (IOException e) {
            // Assert: Verify the exception message clearly indicates a duplicate field error.
            String expectedMessageFragment = "Duplicate field '" + fieldName + "'";
            assertTrue(
                "Exception message should report the duplicate field. Actual message: " + e.getMessage(),
                e.getMessage().contains(expectedMessageFragment)
            );
        }
    }
}