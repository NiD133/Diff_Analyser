package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link VersionUtil} class.
 */
public class VersionUtilTest {

    /**
     * Tests that {@link VersionUtil#versionFor(Class)} can correctly load the
     * version information for a class that belongs to a package with a
     * generated 'PackageVersion' class, such as a class from jackson-core itself.
     */
    @Test
    public void versionFor_whenPackageVersionIsAvailable_shouldReturnCorrectVersion() {
        // Arrange: Use a class from the jackson-core library, which is expected
        // to have its version information available at runtime.
        Class<JsonFactory> classFromJacksonCore = JsonFactory.class;

        // Act: Attempt to load the version for the class.
        Version foundVersion = VersionUtil.versionFor(classFromJacksonCore);

        // Assert: The returned version should not be the default "unknown" version.
        // This confirms that the specific version information for the jackson-core
        // package was successfully located and loaded.
        assertNotNull("Version should not be null", foundVersion);
        assertNotEquals("Should have found a specific version, not the unknown one",
                Version.unknownVersion(), foundVersion);
    }
}