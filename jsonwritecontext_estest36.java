package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link JsonWriteContext} class, focusing on its
 * behavior related to duplicate field detection.
 */
public class JsonWriteContextTest {

    /**
     * Verifies that attempting to write a duplicate field name within the same
     * object context throws a JsonGenerationException when duplicate detection
     * is enabled.
     */
    @Test
    public void writeFieldName_whenDuplicateInObjectContext_shouldThrowException() {
        // Arrange: Set up a context hierarchy that can detect duplicate field names.
        // A DupDetector is required, which can be created from a (potentially null) JsonGenerator.
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        final String fieldName = "duplicateField";

        // Write the field name and a value for the first time, which should succeed.
        try {
            objectContext.writeFieldName(fieldName);
            objectContext.writeValue();
        } catch (JsonGenerationException e) {
            fail("Writing a new field name should not have thrown an exception: " + e.getMessage());
        }

        // Act & Assert: Attempt to write the same field name again and expect an exception.
        try {
            objectContext.writeFieldName(fieldName);
            fail("Expected a JsonGenerationException for duplicate field name, but none was thrown.");
        } catch (JsonGenerationException e) {
            // The exception message should clearly indicate the duplicate field.
            String expectedMessage = "Duplicate field '" + fieldName + "'";
            assertEquals(expectedMessage, e.getOriginalMessage());
        }
    }
}