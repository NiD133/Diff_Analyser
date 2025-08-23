package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.Version;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link VersionUtil} class, focusing on Maven version resolution.
 */
public class VersionUtilTest {

    /**
     * Verifies that {@link VersionUtil#mavenVersionFor(ClassLoader, String, String)}
     * returns {@link Version#unknownVersion()} when the corresponding Maven
     * pom.properties file cannot be found for the given group and artifact IDs.
     */
    @Test
    public void mavenVersionFor_whenPomPropertiesNotFound_returnsUnknownVersion() {
        // Arrange: Define coordinates for a Maven artifact that does not exist.
        final String nonExistentGroupId = "com.example.nonexistent";
        final String nonExistentArtifactId = "non-existent-artifact";
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final Version expectedVersion = Version.unknownVersion();

        // Act: Attempt to load the version for the non-existent artifact.
        Version actualVersion = VersionUtil.mavenVersionFor(classLoader, nonExistentGroupId, nonExistentArtifactId);

        // Assert: The result should be the standard "unknown" version.
        assertEquals(expectedVersion, actualVersion);
    }
}