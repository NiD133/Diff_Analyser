package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link JavaVersion}.
 *
 * <p>These tests focus on the package-private {@code parseMajorJavaVersion} method
 * to verify the version string parsing logic in an environment-independent manner.
 * This approach ensures the tests are robust and not dependent on the JVM running them.
 */
public class JavaVersionTest {

    @Test
    public void parseMajorJavaVersion_shouldReturn8_forJava8VersionString() {
        // Test the legacy "1.8" format
        int version = JavaVersion.parseMajorJavaVersion("1.8.0_202");
        assertEquals(8, version);
    }

    @Test
    public void parseMajorJavaVersion_shouldReturn9_forJava9VersionString() {
        // Test the modern "9" format introduced in Java 9
        int version = JavaVersion.parseMajorJavaVersion("9.0.4");
        assertEquals(9, version);
    }

    @Test
    public void parseMajorJavaVersion_shouldReturn11_forJava11VersionString() {
        // Test a modern, double-digit version format
        int version = JavaVersion.parseMajorJavaVersion("11.0.12");
        assertEquals(11, version);
    }

    @Test
    public void parseMajorJavaVersion_shouldReturn9_forNonStandardDebianVersionString() {
        // As noted in the source code, some distributions use non-standard formats
        int version = JavaVersion.parseMajorJavaVersion("9-debian");
        assertEquals(9, version);
    }

    @Test
    public void parseMajorJavaVersion_shouldReturn17_forSimpleVersionString() {
        // Test a simple version string without any dots
        int version = JavaVersion.parseMajorJavaVersion("17");
        assertEquals(17, version);
    }
}