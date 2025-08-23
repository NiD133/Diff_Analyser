package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

// Note: The original test class name and inheritance are preserved.
// This test was likely auto-generated and has been refactored for clarity.
public class JsonWriteContext_ESTestTest35 extends JsonWriteContext_ESTest_scaffolding {

    /**
     * Verifies that attempting to write a duplicate field name within an object context
     * correctly throws a JsonProcessingException when duplicate detection is active.
     */
    @Test
    public void writingDuplicateFieldNameInObjectShouldThrowException() throws IOException {
        // Arrange: Set up a write context with duplicate detection enabled.
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        final String fieldName = "root";

        // Write the field name and a value for the first time. This should succeed.
        objectContext.writeFieldName(fieldName);
        objectContext.writeValue();

        // Act & Assert: Attempting to write the same field name again should fail.
        try {
            objectContext.writeFieldName(fieldName);
            fail("Expected a JsonProcessingException to be thrown for a duplicate field name, but none was thrown.");
        } catch (JsonProcessingException e) {
            // Verify that the exception message clearly indicates the duplicate field.
            String expectedMessageFragment = "Duplicate field '" + fieldName + "'";
            assertTrue(
                "Exception message should contain '" + expectedMessageFragment + "'. Actual: " + e.getMessage(),
                e.getMessage().contains(expectedMessageFragment)
            );
        }
    }
}