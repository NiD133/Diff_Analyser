package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link JsonLocation} class.
 */
public class JsonLocationTest {

    /**
     * Tests that the equals() method correctly returns false when comparing
     * the special 'NA' (Not Available) location with a different, well-defined location.
     * It also verifies that the constructor correctly initializes the location's properties.
     */
    @Test
    public void testEqualsReturnsFalseForNAComparedWithDifferentLocation() {
        // Arrange
        final JsonLocation naLocation = JsonLocation.NA;

        // Create a different location with a distinct content reference and specific coordinates.
        final ContentReference contentReference = ContentReference.rawReference("test source");
        final JsonLocation customLocation = new JsonLocation(contentReference, 1795L, 500L, 2, 3981);

        // Act
        final boolean areEqual = naLocation.equals(customLocation);

        // Assert
        // First, verify the properties of the custom location to ensure it was created correctly.
        assertEquals("Line number should match constructor argument", 2, customLocation.getLineNr());
        assertEquals("Column number should match constructor argument", 3981, customLocation.getColumnNr());
        assertEquals("Byte offset should match constructor argument", 1795L, customLocation.getByteOffset());
        assertEquals("Char offset should match constructor argument", 500L, customLocation.getCharOffset());

        // Finally, assert that the 'NA' location is not equal to the custom location.
        assertFalse("JsonLocation.NA should not be equal to a different location", areEqual);
    }
}