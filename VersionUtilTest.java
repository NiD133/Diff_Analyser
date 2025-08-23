package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Focused, readable tests for VersionUtil.
 * 
 * Organization:
 * - ParseVersionPartTests: unit-level parsing of a single numeric part
 * - ParseVersionTests: end-to-end parsing of full version strings
 * - VersionDiscoveryTests: discovery via generated PackageVersion and fallbacks
 * - DeprecatedTests: legacy mavenVersionFor support
 */
class VersionUtilTest {

    private static final String GROUP = "group";
    private static final String ARTIFACT = "artifact";

    @Nested
    @DisplayName("parseVersionPart(String)")
    class ParseVersionPartTests {

        @Test
        @DisplayName("parses plain integer")
        void parsesPlainInteger() {
            assertEquals(13, VersionUtil.parseVersionPart("13"));
        }

        @Test
        @DisplayName("stops at first non-digit")
        void stopsAtFirstNonDigit() {
            assertEquals(27, VersionUtil.parseVersionPart("27.8"));
            assertEquals(66, VersionUtil.parseVersionPart("66R"));
        }

        @Test
        @DisplayName("negative numbers yield 0")
        void negativeNumbersYieldZero() {
            assertEquals(0, VersionUtil.parseVersionPart("-3"));
        }
    }

    @Nested
    @DisplayName("parseVersion(String, groupId, artifactId)")
    class ParseVersionTests {

        @Test
        @DisplayName("parses full version with qualifier")
        void parsesFullVersionWithQualifier() {
            Version expected = new Version(1, 2, 15, "foo", GROUP, ARTIFACT);
            Version actual = VersionUtil.parseVersion("1.2.15-foo", GROUP, ARTIFACT);
            assertEquals(expected, actual);
        }

        @Test
        @DisplayName("parses SNAPSHOT qualifier and renders full string")
        void parsesSnapshotQualifier() {
            Version v = VersionUtil.parseVersion("1.2.3-SNAPSHOT", GROUP, ARTIFACT);
            assertEquals("group/artifact/1.2.3-SNAPSHOT", v.toFullString());
        }

        @Test
        @DisplayName("garbage input yields zeroed, non-snapshot, non-unknown Version")
        void garbageInputYieldsZeroedNonSnapshotNonUnknown() {
            String garbage = "#M&+m@569P";
            Version v = VersionUtil.parseVersion(garbage, garbage, "com.fasterxml.jackson.core.util.VersionUtil");

            assertEquals(0, v.getMajorVersion());
            assertEquals(0, v.getMinorVersion());
            assertEquals(0, v.getPatchLevel());
            assertFalse(v.isSnapshot());
            assertFalse(v.isUnknownVersion());
        }

        @Test
        @DisplayName("empty input yields unknownVersion()")
        void emptyInputYieldsUnknownVersion() {
            Version v = VersionUtil.parseVersion("", "", "\"g2AT");
            assertTrue(v.isUnknownVersion());
        }

        @Test
        @DisplayName("null input yields non-snapshot (unknown) Version")
        void nullInputYieldsNonSnapshotUnknown() {
            Version v = VersionUtil.parseVersion(null, "/nUmRN)3", "");
            assertFalse(v.isSnapshot());
        }
    }

    @Nested
    @DisplayName("versionFor(Class)")
    class VersionDiscoveryTests {

        @Test
        @DisplayName("finds PackageVersion next to a Jackson class")
        void findsPackageVersion() {
            assertEquals(PackageVersion.VERSION, VersionUtil.versionFor(UTF8JsonGenerator.class));
        }

        @Test
        @DisplayName("returns unknownVersion() when no PackageVersion class exists")
        void returnsUnknownVersionWhenNoPackageVersion() {
            // [core#248] do not return null
            assertEquals(Version.unknownVersion(), VersionUtil.versionFor(VersionUtilTest.class));
        }
    }

    @Nested
    @DisplayName("Deprecated APIs")
    class DeprecatedTests {

        @SuppressWarnings("deprecation")
        @Test
        @DisplayName("mavenVersionFor reads version from pom.properties on classpath")
        void mavenVersionParsing() {
            Version expected = new Version(1, 2, 3, "SNAPSHOT", "foo.bar", "foo-bar");
            Version actual = VersionUtil.mavenVersionFor(
                    VersionUtilTest.class.getClassLoader(),
                    "foo.bar",
                    "foo-bar"
            );
            assertEquals(expected, actual);
        }
    }
}