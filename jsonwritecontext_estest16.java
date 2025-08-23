package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonWriteContext} class.
 */
public class JsonWriteContextTest {

    /**
     * Verifies that a newly created root context is initialized correctly and that
     * getDupDetector() returns the same instance used for its creation.
     */
    @Test
    public void shouldReturnSameDupDetectorWhenRootContextIsCreated() {
        // Arrange: Create a root-level duplicate detector and use it to create a root context.
        // A null generator is acceptable for creating a root detector.
        DupDetector expectedDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(expectedDetector);

        // Act: Retrieve the duplicate detector from the newly created context.
        DupDetector actualDetector = rootContext.getDupDetector();

        // Assert: Check that the retrieved detector is the exact same instance
        // and that other properties of the root context are set as expected.
        assertSame("The retrieved DupDetector should be the same instance provided during creation.",
                expectedDetector, actualDetector);

        assertEquals("A root context's type description should be 'ROOT'.", "ROOT", rootContext.getTypeDesc());
        assertEquals("A root context should have a nesting depth of 0.", 0, rootContext.getNestingDepth());
        assertEquals("A new context should have an entry count of 0.", 0, rootContext.getEntryCount());
    }
}