package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link VersionUtil} class, focusing on version string parsing logic.
 */
public class VersionUtilTest {

    /**
     * Tests that {@code parseVersionPart} correctly handles an empty string input
     * by returning 0, which is the expected default value for a missing version component.
     */
    @Test
    public void parseVersionPart_shouldReturnZero_whenGivenEmptyString() {
        // Arrange
        String emptyVersionPart = "";
        int expectedVersionPart = 0;

        // Act
        int actualVersionPart = VersionUtil.parseVersionPart(emptyVersionPart);

        // Assert
        assertEquals(expectedVersionPart, actualVersionPart);
    }
}