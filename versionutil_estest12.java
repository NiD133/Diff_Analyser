package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.Version;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link VersionUtil} class.
 * This class replaces the auto-generated test suite for better clarity.
 */
public class VersionUtilTest {

    /**
     * Verifies that parseVersion correctly handles a version string containing
     * non-numeric major and minor components. It should parse these components as 0
     * and still correctly extract the numeric patch level.
     */
    @Test
    public void parseVersion_shouldHandleNonNumericMajorAndMinorParts_andParsePatchCorrectly() {
        // Arrange: Create a version string with non-numeric text for the first two
        // parts and a valid number for the patch level. The separators (';' and '-')
        // are chosen from the set supported by the method.
        final String versionStringWithGarbage = "text;garbage-6-SNAPSHOT";
        final String groupId = "com.example";
        final String artifactId = "my-library";

        // Act: Parse the version string.
        Version parsedVersion = VersionUtil.parseVersion(versionStringWithGarbage, groupId, artifactId);

        // Assert: Check that the numeric parts were parsed correctly and non-numeric parts defaulted to 0.
        assertEquals("Major version should default to 0 for non-numeric input", 0, parsedVersion.getMajorVersion());
        assertEquals("Minor version should default to 0 for non-numeric input", 0, parsedVersion.getMinorVersion());
        assertEquals("Patch level should be parsed correctly from the third part", 6, parsedVersion.getPatchLevel());
        
        // Also verify that groupId and artifactId are stored correctly.
        assertEquals("Group ID should be stored", groupId, parsedVersion.getGroupId());
        assertEquals("Artifact ID should be stored", artifactId, parsedVersion.getArtifactId());
    }
}