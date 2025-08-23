package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Contains tests for the {@link JsonLocation} class, focusing on its construction
 * and state correctness.
 */
public class JsonLocationTest {

    /**
     * Verifies that the JsonLocation constructor correctly initializes all properties
     * and that the corresponding getters return the expected values.
     */
    @Test
    public void constructorShouldSetAllLocationPropertiesCorrectly() {
        // Arrange: Define the location parameters for creating a JsonLocation instance.
        final long expectedCharOffset = 314L;
        final int expectedLineNr = 0;
        final int expectedColumnNr = -2189;
        // This specific constructor defaults the byte offset to -1.
        final long expectedByteOffset = -1L;
        
        // A ContentReference is required, but its internal state is not the focus of this test.
        // Using a standard "unknown" reference is sufficient and clear.
        ContentReference contentReference = ContentReference.unknown();

        // Act: Create a new JsonLocation instance using the constructor under test.
        JsonLocation location = new JsonLocation(contentReference, expectedCharOffset, expectedLineNr, expectedColumnNr);

        // Assert: Confirm that each property of the created object matches the value it was
        // initialized with.
        assertEquals("Line number should match constructor argument", expectedLineNr, location.getLineNr());
        assertEquals("Column number should match constructor argument", expectedColumnNr, location.getColumnNr());
        assertEquals("Character offset should match constructor argument", expectedCharOffset, location.getCharOffset());
        assertEquals("Byte offset should default to -1 for this constructor", expectedByteOffset, location.getByteOffset());
        assertSame("Content reference should be the one provided", contentReference, location.contentReference());
    }
}