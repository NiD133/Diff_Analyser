package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Contains tests for {@link VersionUtil}, focusing on version lookup scenarios.
 */
class VersionUtilTest {

    /**
     * Verifies that {@link VersionUtil#versionFor(Class)} returns {@link Version#unknownVersion()}
     * when a "PackageVersion" class is not found in the target class's package.
     * <p>
     * This behavior is crucial to prevent NullPointerExceptions and provide a predictable
     * default value. This test uses its own class, which resides in a package known
     * not to have a corresponding {@code PackageVersion.java} file.
     *
     * @see <a href="https://github.com/FasterXML/jackson-core/issues/248">[core#248]</a>
     */
    @Test
    void shouldReturnUnknownVersionWhenPackageVersionIsMissing() {
        // Arrange: Use a class from a package that does not contain a "PackageVersion" file.
        // In this case, the test class itself serves as a good example.
        Class<?> classInPackageWithoutVersionInfo = VersionUtilTest.class;

        // Act: Attempt to look up the version for the class.
        Version actualVersion = VersionUtil.versionFor(classInPackageWithoutVersionInfo);

        // Assert: The result should be the standard "unknown" version, not null.
        assertEquals(Version.unknownVersion(), actualVersion,
                "Expected 'unknown version' when a PackageVersion class is not available.");
    }
}