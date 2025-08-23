package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link JsonReadContext} class.
 */
public class JsonReadContextTest {

    /**
     * Tests that a comma is not expected before the first element in a new context.
     * The `expectComma()` method should return false on its first invocation for a given context.
     */
    @Test
    public void expectComma_shouldReturnFalse_forFirstElementInNewContext() {
        // Arrange: Create a parent context and then a new child (array) context.
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext rootContext = JsonReadContext.createRootContext(dupDetector);
        JsonReadContext arrayContext = rootContext.createChildArrayContext(1, 10);

        // A new context should have an entry count of 0.
        assertEquals("Pre-condition: A new context should have zero entries.", 0, arrayContext.getEntryCount());

        // Act: Call the method under test.
        boolean commaExpected = arrayContext.expectComma();

        // Assert: Verify the behavior.
        // A comma is never expected before the first element.
        assertFalse("A comma should not be expected for the first element.", commaExpected);
        
        // The call to expectComma() increments the internal counter.
        assertEquals("Post-condition: Entry count should be 1 after the first check.", 1, arrayContext.getEntryCount());
    }
}