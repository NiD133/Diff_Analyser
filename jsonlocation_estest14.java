package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonLocation} class.
 */
public class JsonLocationTest {

    /**
     * Verifies that the deprecated constructor correctly stores the provided source object
     * and coordinates, and that the corresponding deprecated getter retrieves them.
     * This ensures backward compatibility.
     */
    @Test
    @SuppressWarnings("deprecation") // Intentionally testing deprecated constructor and methods.
    public void deprecatedConstructorShouldCorrectlyStoreSourceAndCoordinates() {
        // Arrange
        Object sourceObject = "Test JSON source content";
        int expectedLine = 15;
        int expectedColumn = 42;
        long expectedCharOffset = 500L;
        long expectedByteOffset = 510L;

        // Act
        // Create a JsonLocation instance using the deprecated constructor that accepts a raw Object.
        JsonLocation location = new JsonLocation(sourceObject, expectedByteOffset, expectedCharOffset, expectedLine, expectedColumn);

        // Assert
        // Verify that the getters return the exact values passed to the constructor.
        assertSame("The source reference should be the same instance passed to the constructor.",
                sourceObject, location.getSourceRef());
        assertEquals("The line number should match the constructor argument.",
                expectedLine, location.getLineNr());
        assertEquals("The column number should match the constructor argument.",
                expectedColumn, location.getColumnNr());
    }
}