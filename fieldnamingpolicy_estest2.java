package com.google.gson;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the static helper methods in {@link FieldNamingPolicy}.
 */
public class FieldNamingPolicyTest {

    /**
     * Verifies that calling {@code separateCamelCase} with an empty string
     * correctly returns an empty string, regardless of the separator used.
     */
    @Test
    public void separateCamelCase_withEmptyInput_shouldReturnEmptyString() {
        // Arrange
        String emptyInput = "";
        char separator = '_';

        // Act
        String result = FieldNamingPolicy.separateCamelCase(emptyInput, separator);

        // Assert
        assertEquals("An empty input string should result in an empty output string.", "", result);
    }
}