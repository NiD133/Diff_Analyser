package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.Version;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the version parsing functionality in {@link VersionUtil}.
 */
public class VersionUtilTest {

    /**
     * Verifies that parseVersion() can extract a minor version number from a string
     * that contains non-numeric characters and uses a slash ('/') as a separator.
     *
     * <p>The expected behavior is:
     * <ul>
     *     <li>The first segment ("iQT[E") is non-numeric, so the major version defaults to 0.</li>
     *     <li>The second segment ("73*QBf") starts with a number, so the minor version is parsed as 73.</li>
     *     <li>There is no third segment, so the patch version defaults to 0.</li>
     * </ul>
     */
    @Test
    public void parseVersionShouldCorrectlyHandleStringWithNonNumericPartsAndSlashSeparator() {
        // Arrange: A version string with a non-numeric prefix, a numeric middle part,
        // and a non-numeric suffix, using a slash as a delimiter.
        String versionStringWithNoise = "iQT[E/73*QBf";
        String groupId = "com.example";
        String artifactId = "test-artifact";

        // Act: Parse the version string.
        Version parsedVersion = VersionUtil.parseVersion(versionStringWithNoise, groupId, artifactId);

        // Assert: Verify that the numeric parts were extracted correctly and non-numeric
        // parts resulted in default values (0).
        assertEquals("Major version should default to 0 for a non-numeric first segment",
                0, parsedVersion.getMajorVersion());
        assertEquals("Minor version should be parsed from the second segment",
                73, parsedVersion.getMinorVersion());
        assertEquals("Patch level should default to 0 as it is missing",
                0, parsedVersion.getPatchLevel());
    }
}