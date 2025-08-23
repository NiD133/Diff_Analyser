package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link VersionUtil} class.
 */
class VersionUtilTest {

    /**
     * Verifies that parsing an empty version string results in an "unknown version".
     * The groupId and artifactId values do not affect the parsing outcome in this case
     * but are provided as typical non-empty values.
     */
    @Test
    void shouldReturnUnknownVersionForEmptyVersionString() {
        // Arrange
        String emptyVersionString = "";
        String groupId = "com.fasterxml.jackson.core";
        String artifactId = "jackson-core";

        // Act
        Version version = VersionUtil.parseVersion(emptyVersionString, groupId, artifactId);

        // Assert
        assertTrue(version.isUnknownVersion(),
                "Parsing an empty version string should result in an unknown version.");
    }
}