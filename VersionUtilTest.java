package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;

import static org.junit.jupiter.api.Assertions.*;

class VersionUtilTest {
    // Tests for version part parsing (individual version components)
    @ParameterizedTest
    @ValueSource(strings = {"13", "27.8", "66R"})
    void parseVersionPart_withValidInput_returnsNumericPrefix(String input) {
        int result = VersionUtil.parseVersionPart(input);
        assertTrue(result > 0, "Should parse positive numeric prefix");
    }

    @ParameterizedTest
    @ValueSource(strings = {"-3", "abc", ""})
    void parseVersionPart_withInvalidInput_returnsZero(String input) {
        assertEquals(0, VersionUtil.parseVersionPart(input));
    }

    // Tests for full version string parsing
    @Test
    void parseVersion_withValidInput_returnsCorrectVersion() {
        // Standard version with qualifier
        Version version = VersionUtil.parseVersion("1.2.15-foo", "group", "artifact");
        assertEquals(new Version(1, 2, 15, "foo", "group", "artifact"), version);
        
        // Snapshot version
        Version snapshotVersion = VersionUtil.parseVersion("1.2.3-SNAPSHOT", "group", "artifact");
        assertEquals("group/artifact/1.2.3-SNAPSHOT", snapshotVersion.toFullString());
    }

    @Test
    void parseVersion_withUnparseableInput_returnsZeroVersion() {
        String unparseableVersion = "#M&+m@569P";
        Version version = VersionUtil.parseVersion(unparseableVersion, "testGroup", "testArtifact");
        
        assertEquals(0, version.getMajorVersion());
        assertEquals(0, version.getMinorVersion());
        assertEquals(0, version.getPatchLevel());
        assertFalse(version.isSnapshot());
        assertFalse(version.isUnknownVersion());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  "})
    void parseVersion_withEmptyInput_returnsUnknownVersion(String input) {
        Version version = VersionUtil.parseVersion(input, "testGroup", "testArtifact");
        assertTrue(version.isUnknownVersion());
    }

    @Test
    void parseVersion_withNullInput_returnsUnknownVersion() {
        Version version = VersionUtil.parseVersion(null, "testGroup", "testArtifact");
        assertTrue(version.isUnknownVersion());
    }

    // Tests for version retrieval functionality
    @Test
    void versionFor_withCoreClass_returnsPackageVersion() {
        Version coreVersion = VersionUtil.versionFor(UTF8JsonGenerator.class);
        assertEquals(PackageVersion.VERSION, coreVersion);
    }

    @Test
    void versionFor_withUnknownClass_returnsUnknownVersion() {
        // Class without PackageVersion should return unknown
        Version unknownVersion = VersionUtil.versionFor(VersionUtilTest.class);
        assertEquals(Version.unknownVersion(), unknownVersion);
    }

    // Test for deprecated functionality
    @SuppressWarnings("deprecation")
    @Test
    void mavenVersionFor_withValidInput_returnsParsedVersion() {
        Version version = VersionUtil.mavenVersionFor(
            VersionUtilTest.class.getClassLoader(), 
            "foo.bar", 
            "foo-bar"
        );
        assertEquals(new Version(1, 2, 3, "SNAPSHOT", "foo.bar", "foo-bar"), version);
    }
}