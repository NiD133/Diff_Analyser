package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.Version;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link VersionUtil} class.
 */
public class VersionUtilTest {

    /**
     * Tests that the deprecated instance method {@code version()} correctly retrieves
     * the version information for the jackson-core component and that it is not a
     * snapshot version. This ensures backward compatibility is maintained.
     */
    @Test
    @SuppressWarnings("deprecation") // Testing a deprecated method for backward compatibility.
    public void version_onInstance_returnsCorrectNonSnapshotVersion() {
        // Arrange
        VersionUtil versionUtil = new VersionUtil();

        // Act
        Version coreVersion = versionUtil.version();

        // Assert
        // This test assumes it runs against a final release of the jackson-core library.
        assertFalse("The version of jackson-core should be a release, not a snapshot.",
                coreVersion.isSnapshot());
    }
}