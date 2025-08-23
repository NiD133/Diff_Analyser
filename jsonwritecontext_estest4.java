package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link JsonWriteContext} class, focusing on its state management.
 */
public class JsonWriteContextTest {

    /**
     * Verifies that hasCurrentName() correctly returns true after a field name has been written
     * to an object context.
     */
    @Test
    public void hasCurrentNameShouldReturnTrueAfterWritingFieldName() throws Exception {
        // Arrange: Create a root context and a child object context.
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        JsonWriteContext objectContext = rootContext.createChildObjectContext();

        // Sanity check: Initially, the context should not have a current name.
        assertFalse("A new object context should not have a current name", objectContext.hasCurrentName());

        // Act: Write a field name to the object context.
        objectContext.writeFieldName("testProperty");

        // Assert: The context should now report that it has a current name.
        assertTrue("hasCurrentName() should return true after a field name is written", objectContext.hasCurrentName());
    }
}