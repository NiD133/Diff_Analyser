package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

/**
 * Tests for {@link JavaVersion}.
 */
public class JavaVersionTest {

    @Test
    public void parseMajorJavaVersion_unrecognizedFormat_returnsDefaultVersion() {
        // The `parseMajorJavaVersion` method is designed to handle version strings
        // that start with a number, such as "1.8.0" or "9.0.4".
        String unrecognizedFormat = "Java9";

        // When the version string format is unrecognized, the method should fall back
        // to a safe default value (6) to prevent runtime errors.
        int expectedDefaultVersion = 6;

        // Act
        int parsedVersion = JavaVersion.parseMajorJavaVersion(unrecognizedFormat);

        // Assert
        assertThat(parsedVersion).isEqualTo(expectedDefaultVersion);
    }
}