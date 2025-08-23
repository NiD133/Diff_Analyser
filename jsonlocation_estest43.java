package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link JsonLocation} class, focusing on equality checks.
 */
public class JsonLocationTest {

    /**
     * Verifies that the special 'Not Applicable' (NA) instance of JsonLocation
     * is not considered equal to a different, custom-defined location.
     */
    @Test
    public void naLocation_shouldNotBeEqualTo_customLocation() {
        // Arrange
        final JsonLocation naLocation = JsonLocation.NA;

        final long charOffset = 500L;
        final int line = 500;
        final int column = 500;
        
        // Create a distinct location. Using the modern, non-deprecated constructor.
        // The original test used a constructor that implicitly set the byte offset to -1.
        final JsonLocation customLocation = new JsonLocation(ContentReference.unknown(), -1L, charOffset, line, column);

        // Pre-verify the state of the custom location to ensure it was created as expected.
        assertEquals("Line number should match", line, customLocation.getLineNr());
        assertEquals("Column number should match", column, customLocation.getColumnNr());
        assertEquals("Character offset should match", charOffset, customLocation.getCharOffset());
        assertEquals("Byte offset should be -1", -1L, customLocation.getByteOffset());

        // Act
        boolean areEqual = naLocation.equals(customLocation);

        // Assert
        assertFalse("JsonLocation.NA should not be equal to a different location instance", areEqual);
    }
}