package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link VersionUtil} class.
 */
class VersionUtilTest {

    /**
     * Verifies that parsing a version string that does not start with digits
     * results in a version of 0.0.0.
     * <p>
     * The implementation of {@code VersionUtil.parseVersion} attempts to parse numbers
     * until it hits a non-digit. If the string starts with a non-digit, the parsed value is 0.
     */
    @Test
    void parseVersion_withNonNumericString_shouldReturnVersionZero() {
        // Arrange
        String nonNumericVersionString = "invalid-version";
        String groupId = "com.example";
        String artifactId = "my-artifact";

        // Act
        Version version = VersionUtil.parseVersion(nonNumericVersionString, groupId, artifactId);

        // Assert
        assertNotNull(version, "The parsed version should not be null.");

        assertAll("A non-numeric version string should default to version 0.0.0",
            () -> assertEquals(0, version.getMajorVersion(), "Major version should be 0"),
            () -> assertEquals(0, version.getMinorVersion(), "Minor version should be 0"),
            () -> assertEquals(0, version.getPatchLevel(), "Patch level should be 0"),
            () -> assertFalse(version.isSnapshot(), "Version should not be a snapshot"),
            // The method creates a new Version(0,0,0,...) instance, which is distinct
            // from the singleton returned by Version.unknownVersion().
            () -> assertFalse(version.isUnknownVersion(), "Version should not be the 'unknown' singleton")
        );
    }
}