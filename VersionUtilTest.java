package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the utility class {@link VersionUtil}.
 */
@DisplayName("VersionUtil")
class VersionUtilTest {

    @Nested
    @DisplayName("Method: parseVersionPart(String)")
    class ParseVersionPartTest {

        @ParameterizedTest(name = "Input: \"{0}\" -> Expected: {1}")
        @CsvSource({
                "13,    13",  // Should parse a simple integer
                "27.8,  27",  // Should stop parsing at the first non-digit
                "66R,   66",  // Should stop parsing at a trailing character
                "-3,    0",   // Should treat negative numbers as zero
                "foo,   0",   // Should return zero for non-numeric input
                "'',    0"    // Should return zero for an empty string
        })
        void shouldParseIntegerComponent(String input, int expected) {
            assertEquals(expected, VersionUtil.parseVersionPart(input));
        }
    }

    @Nested
    @DisplayName("Method: parseVersion(String, String, String)")
    class ParseVersionTest {

        @Test
        void shouldParseStandardVersionString() {
            Version expected = new Version(1, 2, 15, "foo", "group", "artifact");
            Version actual = VersionUtil.parseVersion("1.2.15-foo", "group", "artifact");
            assertEquals(expected, actual);
        }

        @Test
        void shouldParseSnapshotVersionString() {
            Version version = VersionUtil.parseVersion("1.2.3-SNAPSHOT", "group", "artifact");

            assertAll("Snapshot Version",
                    () -> assertEquals(1, version.getMajorVersion()),
                    () -> assertEquals(2, version.getMinorVersion()),
                    () -> assertEquals(3, version.getPatchLevel()),
                    () -> assertTrue(version.isSnapshot()),
                    () -> assertEquals("SNAPSHOT", version.getSnapshotInfo()),
                    () -> assertEquals("group", version.getGroupId()),
                    () -> assertEquals("artifact", version.getArtifactId())
            );
        }

        @Test
        void shouldReturnZeroVersionForMalformedString() {
            // When parsing fails on a non-empty version string, it defaults to 0.0.0.
            // It is not "unknown" because an attempt to parse was made.
            Version version = VersionUtil.parseVersion("not-a-version", "some-group", "some-artifact");

            assertEquals(0, version.getMajorVersion());
            assertEquals(0, version.getMinorVersion());
            assertEquals(0, version.getPatchLevel());
            assertFalse(version.isSnapshot());
            assertFalse(version.isUnknownVersion(), "Should be a parsed 'zero' version, not an 'unknown' version");
        }

        @Test
        void shouldReturnUnknownVersionForNullString() {
            Version version = VersionUtil.parseVersion(null, "some-group", "some-artifact");
            assertEquals(Version.unknownVersion(), version);
            assertTrue(version.isUnknownVersion());
        }

        @Test
        void shouldReturnUnknownVersionForEmptyString() {
            Version version = VersionUtil.parseVersion("", "some-group", "some-artifact");
            assertEquals(Version.unknownVersion(), version);
            assertTrue(version.isUnknownVersion());
        }
    }

    @Nested
    @DisplayName("Method: versionFor(Class)")
    class VersionForTest {

        @Test
        void shouldFindVersionForClassWithPackageVersion() {
            // UTF8JsonGenerator is in a package that has a PackageVersion class, which should be found.
            Version expectedVersion = PackageVersion.VERSION;
            Version actualVersion = VersionUtil.versionFor(UTF8JsonGenerator.class);

            assertEquals(expectedVersion, actualVersion);
        }

        @Test
        void shouldReturnUnknownVersionForClassWithoutPackageVersion() {
            // This test class itself does not have a sibling PackageVersion class.
            // The method should return the singleton "unknown" version, not null.
            Version version = VersionUtil.versionFor(VersionUtilTest.class);

            assertEquals(Version.unknownVersion(), version);
            assertTrue(version.isUnknownVersion());
        }
    }

    @Nested
    @DisplayName("Deprecated Methods")
    class DeprecatedApiTest {

        @Test
        @SuppressWarnings("deprecation")
        void shouldParseMavenVersionFromPomProperties() {
            // This test relies on a "pom.properties" file being available on the classpath
            // at META-INF/maven/foo.bar/foo-bar/pom.properties for test purposes.
            Version expected = new Version(1, 2, 3, "SNAPSHOT", "foo.bar", "foo-bar");

            Version actual = VersionUtil.mavenVersionFor(
                    VersionUtilTest.class.getClassLoader(), "foo.bar", "foo-bar");

            assertEquals(expected, actual);
        }
    }
}