package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the package-private helper methods in {@link JavaVersion}.
 */
public class JavaVersionTest {

    /**
     * Verifies that parseMajorJavaVersion correctly handles a version string
     * that consists of only a single digit, such as "1".
     */
    @Test
    public void parseMajorJavaVersion_withSingleDigitString_returnsCorrectVersion() {
        // Arrange
        String javaVersionString = "1";
        int expectedMajorVersion = 1;

        // Act
        int actualMajorVersion = JavaVersion.parseMajorJavaVersion(javaVersionString);

        // Assert
        assertEquals(expectedMajorVersion, actualMajorVersion);
    }
}