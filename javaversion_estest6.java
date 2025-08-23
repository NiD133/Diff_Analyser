package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link JavaVersion}.
 */
public class JavaVersionTest {

    /**
     * Tests that `parseMajorJavaVersion` correctly extracts the major version from a
     * legacy-style version string (e.g., "1.x") even when it is followed by
     * non-numeric characters. The method is expected to parse the first number it finds.
     */
    @Test
    public void parseMajorJavaVersion_shouldParseLegacyVersionWithTrailingText() {
        // Arrange
        // This input simulates a version string that starts with "1." but has
        // non-numeric characters immediately after.
        String versionStringWithTrailingText = "1.&N<+EILs/Cn\",";
        int expectedMajorVersion = 1;

        // Act
        int actualMajorVersion = JavaVersion.parseMajorJavaVersion(versionStringWithTrailingText);

        // Assert
        assertEquals("Should extract major version '1' from the start of the string.",
                expectedMajorVersion, actualMajorVersion);
    }
}