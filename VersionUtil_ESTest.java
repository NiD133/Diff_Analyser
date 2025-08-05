package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link VersionUtil} class.
 */
public class VersionUtilTest {

    private static final Version UNKNOWN_VERSION = Version.unknownVersion();

    // --- Tests for parseVersionPart() ---

    @Test
    public void parseVersionPart_shouldParseLeadingDigits() {
        // Asserts that parsing stops at the first non-digit character.
        assertEquals(123, VersionUtil.parseVersionPart("123xyz"));
        assertEquals(45, VersionUtil.parseVersionPart("45"));
        assertEquals(0, VersionUtil.parseVersionPart("0-SNAPSHOT"));
    }

    @Test
    public void parseVersionPart_shouldReturnZero_forEmptyString() {
        assertEquals(0, VersionUtil.parseVersionPart(""));
    }

    @Test
    public void parseVersionPart_shouldReturnZero_whenStringStartsWithNonDigit() {
        assertEquals(0, VersionUtil.parseVersionPart("abc123"));
        assertEquals(0, VersionUtil.parseVersionPart("T*A{.*6/lD:1tm E_"));
    }

    @Test(expected = NullPointerException.class)
    public void parseVersionPart_shouldThrowException_forNullInput() {
        VersionUtil.parseVersionPart(null);
    }

    // --- Tests for parseVersion() ---

    @Test
    public void parseVersion_shouldParseStandardVersionString() {
        Version version = VersionUtil.parseVersion("2.9.8", "group", "artifact");
        assertEquals(new Version(2, 9, 8, null, "group", "artifact"), version);
    }

    @Test
    public void parseVersion_shouldParseSnapshotVersionString() {
        Version version = VersionUtil.parseVersion("2.9.8-SNAPSHOT", "group", "artifact");
        assertEquals(new Version(2, 9, 8, "SNAPSHOT", "group", "artifact"), version);
        assertTrue(version.isSnapshot());
    }

    @Test
    public void parseVersion_shouldHandlePartialVersionString() {
        // A version with only major and minor components
        Version version = VersionUtil.parseVersion("3.5", "group", "artifact");
        assertEquals(new Version(3, 5, 0, null, "group", "artifact"), version);
    }

    @Test
    public void parseVersion_shouldHandleMalformedVersionStringWithNonNumericParts() {
        // If parts are not numeric, they should be parsed as 0.
        Version version = VersionUtil.parseVersion("foo.1.bar-baz", "group", "artifact");
        assertEquals(new Version(0, 1, 0, "baz", "group", "artifact"), version);
    }

    @Test
    public void parseVersion_shouldReturnUnknownVersion_forNullString() {
        Version version = VersionUtil.parseVersion(null, null, null);
        assertTrue(version.isUknownVersion());
    }

    @Test
    public void parseVersion_shouldReturnUnknownVersion_forEmptyString() {
        Version version = VersionUtil.parseVersion("", "group", "artifact");
        assertTrue(version.isUknownVersion());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void parseVersion_shouldThrowException_forSeparatorOnlyString() {
        // A string like ";" splits into an empty array, causing an AIOOBE.
        VersionUtil.parseVersion(";", "group", "artifact");
    }

    // --- Tests for version loading ---

    @Test
    public void versionFor_shouldLoadVersionFromPackageVersionClass() {
        // This test relies on the jackson-core jar being on the classpath,
        // which contains a PackageVersion class for com.fasterxml.jackson.core.
        Version version = VersionUtil.versionFor(JsonFactory.class);
        assertNotEquals(UNKNOWN_VERSION, version);
        // The exact version depends on the dependency, but we can check it's not unknown.
        assertEquals("com.fasterxml.jackson.core", version.getGroupId());
        assertEquals("jackson-core", version.getArtifactId());
    }

    @Test
    public void packageVersionFor_shouldReturnUnknownVersion_whenPackageVersionClassNotFound() {
        // The java.lang package does not have a PackageVersion class.
        Version version = VersionUtil.packageVersionFor(Object.class);
        assertTrue(version.isUknownVersion());
    }

    @Test
    public void mavenVersionFor_shouldReturnUnknownVersion_whenPropertiesNotFound() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // Using an invalid group/artifact ID ensures the pom.properties file won't be found.
        Version version = VersionUtil.mavenVersionFor(classLoader, "invalid-group", "invalid-artifact");
        assertTrue(version.isUknownVersion());
    }

    @Test(expected = NullPointerException.class)
    public void mavenVersionFor_shouldThrowException_forNullClassLoader() {
        VersionUtil.mavenVersionFor(null, "group", "artifact");
    }

    // --- Tests for utility methods ---

    @Test(expected = RuntimeException.class)
    public void throwInternal_shouldAlwaysThrowRuntimeException() {
        VersionUtil.throwInternal();
    }

    @Test(expected = RuntimeException.class)
    public void throwInternalReturnAny_shouldAlwaysThrowRuntimeException() {
        VersionUtil.throwInternalReturnAny();
    }

    @Test
    public void constructor_and_versionMethod_shouldReturnVersion() {
        // The instance method version() should return the package version.
        VersionUtil util = new VersionUtil();
        Version version = util.version();
        assertNotEquals(UNKNOWN_VERSION, version);
    }
}