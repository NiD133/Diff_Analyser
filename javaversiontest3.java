package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

/**
 * Tests for {@link JavaVersion}.
 */
public class JavaVersionTest {

    /**
     * Tests parsing of the legacy Java version string format (e.g., "1.x.y").
     * Before Java 9, versions were reported as "1.7", "1.8", etc. The major
     * version in this scheme is the second number (e.g., 7 for "1.7.0").
     */
    @Test
    public void parseMajorJavaVersion_legacyFormat_returnsMajorVersion() {
        // Arrange
        String legacyJavaVersionString = "1.7.0";
        int expectedMajorVersion = 7;

        // Act
        int actualMajorVersion = JavaVersion.parseMajorJavaVersion(legacyJavaVersionString);

        // Assert
        assertThat(actualMajorVersion).isEqualTo(expectedMajorVersion);
    }
}