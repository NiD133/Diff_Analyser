package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for class {@link VersionUtil}.
 *
 * @see VersionUtil
 */
class VersionUtilTest {

    /**
     * Test parsing of individual version parts.
     */
    @Test
    void testParseVersionPart() {
        assertEquals(13, VersionUtil.parseVersionPart("13"), "Should parse integer part correctly");
        assertEquals(27, VersionUtil.parseVersionPart("27.8"), "Should parse leading integer part correctly");
        assertEquals(0, VersionUtil.parseVersionPart("-3"), "Should return 0 for negative parts");
        assertEquals(66, VersionUtil.parseVersionPart("66R"), "Should parse leading integer part correctly even with suffix");
    }

    /**
     * Test parsing of full version strings.
     */
    @Test
    void testParseVersion() {
        Version expectedVersion = new Version(1, 2, 15, "foo", "group", "artifact");
        Version parsedVersion = VersionUtil.parseVersion("1.2.15-foo", "group", "artifact");
        assertEquals(expectedVersion, parsedVersion, "Parsed version should match expected version");

        Version snapshotVersion = VersionUtil.parseVersion("1.2.3-SNAPSHOT", "group", "artifact");
        assertEquals("group/artifact/1.2.3-SNAPSHOT", snapshotVersion.toFullString(), "Full string should match expected format");
    }

    /**
     * Test parsing of invalid version strings resulting in a version with major, minor, and patch set to zero.
     */
    @Test
    void testParseInvalidVersionString() {
        Version version = VersionUtil.parseVersion("#M&+m@569P", "#M&+m@569P", "com.fasterxml.jackson.core.util.VersionUtil");

        assertEquals(0, version.getMajorVersion(), "Major version should be 0 for invalid input");
        assertEquals(0, version.getMinorVersion(), "Minor version should be 0 for invalid input");
        assertEquals(0, version.getPatchLevel(), "Patch level should be 0 for invalid input");
        assertFalse(version.isSnapshot(), "Should not be a snapshot version");
        assertFalse(version.isUnknownVersion(), "Should not be an unknown version");
    }

    /**
     * Test parsing of an empty version string results in an unknown version.
     */
    @Test
    void testParseEmptyVersionString() {
        Version version = VersionUtil.parseVersion("", "", "\"g2AT");
        assertTrue(version.isUnknownVersion(), "Empty version string should result in unknown version");
    }

    /**
     * Test parsing of a null version string with an empty artifact ID.
     */
    @Test
    void testParseNullVersionString() {
        Version version = VersionUtil.parseVersion(null, "/nUmRN)3", "");
        assertFalse(version.isSnapshot(), "Null version string should not be a snapshot");
    }

    /**
     * Test that the package version matches the version for a known class.
     */
    @Test
    void testPackageVersionMatches() {
        assertEquals(PackageVersion.VERSION, VersionUtil.versionFor(UTF8JsonGenerator.class), "Package version should match expected version");
    }

    /**
     * Test that versionFor returns unknown version for unknown classes.
     */
    @Test
    void testVersionForUnknownClass() {
        assertEquals(Version.unknownVersion(), VersionUtil.versionFor(VersionUtilTest.class), "Unknown class should return unknown version");
    }

    /**
     * Test deprecated maven version parsing functionality.
     */
    @SuppressWarnings("deprecation")
    @Test
    void testMavenVersionParsing() {
        Version expectedVersion = new Version(1, 2, 3, "SNAPSHOT", "foo.bar", "foo-bar");
        Version parsedVersion = VersionUtil.mavenVersionFor(VersionUtilTest.class.getClassLoader(), "foo.bar", "foo-bar");
        assertEquals(expectedVersion, parsedVersion, "Parsed maven version should match expected version");
    }
}