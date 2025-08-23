package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link VersionUtil} class, focusing on parsing logic.
 */
public class VersionUtilTest {

    /**
     * Verifies that {@code parseVersionPart} correctly extracts an integer
     * from the beginning of a string and stops parsing at the first
     * non-digit character.
     */
    @Test
    public void parseVersionPartShouldExtractLeadingIntegerAndIgnoreRestOfString() {
        // Arrange
        // An input string that starts with a number followed by various non-digit characters.
        String versionString = "3$Qtt~IU2x^1~fgh4]+";
        int expectedNumber = 3;

        // Act
        int actualNumber = VersionUtil.parseVersionPart(versionString);

        // Assert
        assertEquals("Should parse the initial number '3' and ignore the rest of the string.",
                expectedNumber, actualNumber);
    }
}