package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link VersionUtil} class.
 *
 * This test suite focuses on verifying the version lookup functionality.
 */
// The class is renamed from "VersionUtilTestTest7" to the standard "VersionUtilTest"
// for better clarity and adherence to conventions.
class VersionUtilTest {

    /**
     * Verifies that {@link VersionUtil#versionFor(Class)} correctly retrieves version
     * information from the 'PackageVersion' class located in the same package
     * as the provided class.
     */
    @Test
    @DisplayName("versionFor() should return the version from the corresponding PackageVersion class")
    void versionFor_shouldReturnVersionFromPackageVersionClass() {
        // ARRANGE
        // The method under test, versionFor(), is expected to find the version information
        // for the package that contains the given class.
        // We use UTF8JsonGenerator as an example class from the 'com.fasterxml.jackson.core.json' package.
        final Class<?> classInPackageWithVersion = UTF8JsonGenerator.class;

        // The expected version is defined in PackageVersion.VERSION, which is in the same package.
        final Version expectedVersion = PackageVersion.VERSION;

        // ACT
        // Retrieve the version using the utility method.
        final Version actualVersion = VersionUtil.versionFor(classInPackageWithVersion);

        // ASSERT
        // The retrieved version should match the one defined in the package's PackageVersion class.
        assertEquals(expectedVersion, actualVersion,
                "The version should match the one defined in the package's PackageVersion class.");
    }
}