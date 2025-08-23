package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.Version;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link VersionUtil} class, focusing on its version parsing capabilities.
 */
public class VersionUtilTest {

    /**
     * Verifies that calling {@code VersionUtil.parseVersion} with all null arguments
     * correctly returns the "unknown" version instance. The "unknown" version is
     * expected to have default values, such as 0 for version numbers and empty
     * strings for group and artifact IDs.
     */
    @Test
    public void parseVersion_withAllNullInputs_shouldReturnUnknownVersion() {
        // Arrange
        String nullVersionString = null;
        String nullGroupId = null;
        String nullArtifactId = null;

        // Act
        Version resultVersion = VersionUtil.parseVersion(nullVersionString, nullGroupId, nullArtifactId);

        // Assert
        // The method should return the singleton 'unknownVersion' instance.
        assertNotNull("The result of parseVersion should not be null.", resultVersion);
        assertTrue("The returned version should be identified as 'unknown'.", resultVersion.isUnknownVersion());

        // Explicitly check the properties of the unknown version for clarity.
        assertEquals("Group ID should be an empty string for an unknown version.", "", resultVersion.getGroupId());
        assertEquals("Artifact ID should be an empty string for an unknown version.", "", resultVersion.getArtifactId());
        assertEquals("Major version should be 0 for an unknown version.", 0, resultVersion.getMajorVersion());
    }
}