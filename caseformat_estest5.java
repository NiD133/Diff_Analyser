package com.google.common.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for {@link CaseFormat}.
 */
public class CaseFormatTest {

    /**
     * Tests that CaseFormat.values() returns all expected enum constants.
     * This test acts as a safeguard to ensure that if a new format is added or removed,
     * developers are reminded to update any dependent logic.
     */
    @Test
    public void values_shouldReturnAllFiveDefinedFormats() {
        // Arrange: The CaseFormat enum is expected to have five constants:
        // 1. LOWER_HYPHEN
        // 2. LOWER_UNDERSCORE
        // 3. LOWER_CAMEL
        // 4. UPPER_CAMEL
        // 5. UPPER_UNDERSCORE
        final int expectedNumberOfFormats = 5;

        // Act
        CaseFormat[] allFormats = CaseFormat.values();

        // Assert
        assertEquals(
            "The number of enum constants in CaseFormat has changed.",
            expectedNumberOfFormats,
            allFormats.length);
    }
}