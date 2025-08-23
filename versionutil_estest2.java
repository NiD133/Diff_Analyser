package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link VersionUtil} class, focusing on version string parsing.
 */
public class VersionUtilTest {

    /**
     * Tests that {@code parseVersionPart} correctly extracts an integer from the beginning
     * of a string, stopping at the first non-digit character. This is a common scenario
     * when parsing version components like "2.8-SNAPSHOT".
     */
    @Test
    public void parseVersionPartShouldStopAtFirstNonDigit() {
        // Arrange
        // A typical version-like string where a number is followed by a non-digit suffix.
        String versionStringWithSuffix = "9-SNAPSHOT";
        int expectedVersionPart = 9;

        // Act
        int actualVersionPart = VersionUtil.parseVersionPart(versionStringWithSuffix);

        // Assert
        assertEquals("Should parse the leading integer '9' and ignore the '-SNAPSHOT' suffix.",
                expectedVersionPart, actualVersionPart);
    }
}