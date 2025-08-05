package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link JavaVersion}.
 * Focuses on testing the version string parsing logic and environment checks.
 */
public class JavaVersionTest {

    // --- Tests for parseMajorJavaVersion ---

    @Test
    public void parseMajorJavaVersion_withModernFormat_returnsMajorVersion() {
        // For modern Java versions (9 and later), the version is the first number.
        assertEquals(9, JavaVersion.parseMajorJavaVersion("9.0.4"));
        assertEquals(10, JavaVersion.parseMajorJavaVersion("10.0.1"));
        assertEquals(11, JavaVersion.parseMajorJavaVersion("11"));
    }

    @Test
    public void parseMajorJavaVersion_withLegacyFormat_returnsMinorVersion() {
        // For legacy Java versions (e.g., 1.8), the major version is the second number.
        assertEquals(8, JavaVersion.parseMajorJavaVersion("1.8.0_211"));
        assertEquals(7, JavaVersion.parseMajorJavaVersion("1.7.0"));
    }

    @Test
    public void parseMajorJavaVersion_withNonNumericSuffix_parsesLeadingDigits() {
        // The parser should extract the initial numeric part of the version string.
        assertEquals(9, JavaVersion.parseMajorJavaVersion("9-Debian"));
        assertEquals(13, JavaVersion.parseMajorJavaVersion("13-ea+13"));
    }

    @Test
    public void parseMajorJavaVersion_withLeadingZero_parsesCorrectly() {
        // A version string like "0" is unusual but should be parsed as 0.
        assertEquals(0, JavaVersion.parseMajorJavaVersion("0Q?"));
    }

    @Test
    public void parseMajorJavaVersion_withEmptyString_returnsDefaultVersion() {
        // The implementation falls back to a default version (6) for unparseable strings.
        // This is a safe default, representing an old, pre-Java 9 version.
        assertEquals(6, JavaVersion.parseMajorJavaVersion(""));
    }

    @Test(expected = NullPointerException.class)
    public void parseMajorJavaVersion_withNullInput_throwsNullPointerException() {
        JavaVersion.parseMajorJavaVersion(null);
    }

    // --- Tests for environment-dependent methods ---

    @Test
    public void getMajorJavaVersion_returnsVersionOfRunningJvm() {
        // This test is dependent on the JVM environment where it's executed.
        // We assert a reasonable minimum version rather than a hardcoded one.
        int majorVersion = JavaVersion.getMajorJavaVersion();
        assertTrue("Expected major Java version to be at least 8, but was " + majorVersion, majorVersion >= 8);
    }

    @Test
    public void isJava9OrLater_returnsValueConsistentWithGetMajorJavaVersion() {
        // This test verifies the internal consistency of the class.
        // It ensures isJava9OrLater correctly reflects the result of getMajorJavaVersion(),
        // regardless of the JVM it runs on.
        boolean expected = JavaVersion.getMajorJavaVersion() >= 9;
        assertEquals(expected, JavaVersion.isJava9OrLater());
    }
}