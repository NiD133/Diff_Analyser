package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.Version;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link VersionUtil} class, focusing on version string parsing.
 */
public class VersionUtilTest {

    /**
     * Tests that {@link VersionUtil#parseVersion(String, String, String)} correctly handles
     * a version string containing non-numeric parts.
     *
     * <p>The expected behavior is that non-numeric major, minor, and patch components
     * are parsed as zero, and the fourth component is correctly identified as snapshot information.
     */
    @Test
    public void parseVersion_withNonNumericParts_shouldDefaultVersionsToZeroAndSetSnapshot() {
        // Arrange
        // A version string with non-numeric parts to test the parser's robustness.
        // The parser should extract leading digits from each part, defaulting to 0 if none are found.
        String nonNumericVersionString = "alpha.beta.gamma.SNAPSHOT";
        String expectedGroupId = "com.example";
        String expectedArtifactId = "my-artifact";

        // Act
        Version parsedVersion = VersionUtil.parseVersion(nonNumericVersionString, expectedGroupId, expectedArtifactId);

        // Assert
        // Verify that the non-numeric version parts were parsed as 0.
        assertEquals(0, parsedVersion.getMajorVersion());
        assertEquals(0, parsedVersion.getMinorVersion());
        assertEquals(0, parsedVersion.getPatchVersion());

        // Verify that the fourth part of the string was correctly treated as snapshot info.
        assertTrue("The version should be identified as a snapshot", parsedVersion.isSnapshot());
        assertEquals("SNAPSHOT", parsedVersion.getSnapshotInfo());

        // Verify that group and artifact IDs were preserved.
        assertEquals(expectedGroupId, parsedVersion.getGroupId());
        assertEquals(expectedArtifactId, parsedVersion.getArtifactId());
    }
}