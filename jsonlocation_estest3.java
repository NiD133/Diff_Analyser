package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Contains unit tests for the {@link JsonLocation} class.
 */
public class JsonLocationTest {

    /**
     * Tests that the {@code equals()} method returns {@code false} for two {@link JsonLocation}
     * instances that have different properties.
     * <p>
     * This test case creates two locations that differ in their source reference,
     * byte offset, character offset, and column number, but share the same line number.
     * This ensures that the {@code equals()} implementation checks all relevant fields.
     */
    @Test
    public void equalsShouldReturnFalseWhenPropertiesDiffer() {
        // Arrange
        // Location 1: Based on an 'unknown' content source. This mimics the original
        // test's use of a null source object.
        JsonLocation location1 = new JsonLocation(ContentReference.unknown(),
                -1L,  // byte offset
                500L, // char offset
                500,  // line number
                500); // column number

        // Location 2: Based on a 'redacted' content source with different offsets.
        JsonLocation location2 = new JsonLocation(ContentReference.redacted(),
                1127L, // byte offset
                -304L, // char offset
                500,   // line number
                92);   // column number

        // Sanity-check that location2 was constructed with the expected values.
        // This confirms the test setup is correct before we test the equals() method.
        assertEquals(1127L, location2.getByteOffset());
        assertEquals(-304L, location2.getCharOffset());
        assertEquals(500, location2.getLineNr());
        assertEquals(92, location2.getColumnNr());

        // Act
        boolean areEqual = location1.equals(location2);

        // Assert
        assertFalse("equals() should return false for locations with different properties.", areEqual);
    }
}