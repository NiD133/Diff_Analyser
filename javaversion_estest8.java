package com.google.gson.internal;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link JavaVersion}.
 */
public class JavaVersionTest {

    /**
     * Verifies that getMajorJavaVersion() correctly identifies the major version
     * of the current Java runtime environment.
     *
     * <p>This test is environment-independent; it dynamically determines the expected
     * version from the "java.version" system property rather than asserting a
     * hardcoded value. This ensures the test remains valid when run on different
     * Java versions (e.g., Java 8, 11, 17).</p>
     */
    @Test
    public void getMajorJavaVersion_shouldReturnCurrentRuntimeVersion() {
        // Arrange
        // The class under test provides a package-private helper for parsing,
        // which we can use to determine the expected version for the current runtime.
        String javaVersionString = System.getProperty("java.version");
        int expectedMajorVersion = JavaVersion.parseMajorJavaVersion(javaVersionString);

        // Act
        int actualMajorVersion = JavaVersion.getMajorJavaVersion();

        // Assert
        assertEquals(
            "The detected major Java version should match the version parsed from the 'java.version' system property.",
            expectedMajorVersion,
            actualMajorVersion);
    }
}