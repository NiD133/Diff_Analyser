package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link JsonLocation} class.
 */
public class JsonLocationTest {

    /**
     * Tests that two JsonLocation objects are not considered equal if they have the same
     * line and column numbers but differ in their byte or character offsets.
     */
    @Test
    public void testEqualsReturnsFalseForDifferentOffsets() {
        // Arrange
        final int commonLine = 500;
        final int commonColumn = 500;
        final ContentReference commonSource = ContentReference.unknown();

        // Create two locations with the same source, line, and column, but different offsets.
        JsonLocation locationA = new JsonLocation(commonSource, -1L, 500L, commonLine, commonColumn);
        JsonLocation locationB = new JsonLocation(commonSource, 344L, 0L, commonLine, commonColumn);

        // Act & Assert
        // The locations should not be equal because their byte and char offsets differ.
        assertNotEquals(locationA, locationB);
    }
}