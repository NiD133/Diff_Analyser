package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link JsonWriteContext} class, focusing on its state management
 * when writing field names.
 */
public class JsonWriteContextRefactoredTest {

    /**
     * Verifies that calling writeFieldName(null) throws a NullPointerException.
     * The underlying DupDetector, which is responsible for tracking field names,
     * does not permit null keys.
     */
    @Test
    public void writeFieldName_whenGivenNullName_shouldThrowNullPointerException() throws JsonProcessingException {
        // Arrange: Create an object context that is ready to accept a new field name.
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        JsonWriteContext objectContext = rootContext.createChildObjectContext();

        // Write one valid field-value pair to transition the context to a state
        // where it expects another field name.
        objectContext.writeFieldName("firstField");
        objectContext.writeValue();

        // Act & Assert: Attempting to write a null field name should fail.
        try {
            objectContext.writeFieldName(null);
            fail("Expected a NullPointerException to be thrown, but it was not.");
        } catch (NullPointerException e) {
            // This is the expected behavior. The test passes.
        }
    }
}