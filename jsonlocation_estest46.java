package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonLocation} class, focusing on its constructors and accessors.
 */
public class JsonLocationTest {

    /**
     * Verifies that the constructor correctly initializes a JsonLocation instance
     * and that the accessor methods return the expected values.
     */
    @Test
    public void shouldStoreAndReturnLocationDetailsCorrectly() {
        // --- Arrange ---
        // Define clear, descriptive variables for the location parameters.
        final Object sourceObject = "test-source";
        final ContentReference contentReference = ContentReference.rawReference(false, sourceObject);
        final long expectedCharOffset = 100L;
        final int expectedLine = 5;
        final int expectedColumn = 12;

        // --- Act ---
        // Create the JsonLocation instance using the modern, non-deprecated constructor.
        // This specific constructor sets the byte offset to a default of -1.
        final JsonLocation location = new JsonLocation(contentReference, expectedCharOffset, expectedLine, expectedColumn);

        // --- Assert ---
        // Verify that each getter returns the value provided during construction.
        // Adding descriptive messages to assertions helps diagnose failures quickly.
        assertEquals("Line number should match the constructor argument",
                expectedLine, location.getLineNr());
        assertEquals("Column number should match the constructor argument",
                expectedColumn, location.getColumnNr());
        assertEquals("Character offset should match the constructor argument",
                expectedCharOffset, location.getCharOffset());

        // The constructor used does not take a byte offset, so it defaults to -1.
        // This assertion explicitly confirms that default behavior.
        assertEquals("Byte offset should default to -1",
                -1L, location.getByteOffset());
    }
}