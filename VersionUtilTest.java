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
class VersionUtilTest
{
    // Tests for parseVersionPart method
    
    @Test
    void parseVersionPart_WithSimpleNumber_ReturnsNumber() {
        int result = VersionUtil.parseVersionPart("13");
        
        assertEquals(13, result);
    }

    @Test
    void parseVersionPart_WithNumberFollowedByDot_ReturnsNumberBeforeDot() {
        int result = VersionUtil.parseVersionPart("27.8");
        
        assertEquals(27, result);
    }

    @Test
    void parseVersionPart_WithNegativeNumber_ReturnsZero() {
        int result = VersionUtil.parseVersionPart("-3");
        
        assertEquals(0, result);
    }

    @Test
    void parseVersionPart_WithNumberFollowedByLetter_ReturnsNumber() {
        int result = VersionUtil.parseVersionPart("66R");
        
        assertEquals(66, result);
    }

    // Tests for parseVersion method

    @Test
    void parseVersion_WithValidVersionString_ReturnsCorrectVersion() {
        Version expectedVersion = new Version(1, 2, 15, "foo", "group", "artifact");
        
        Version actualVersion = VersionUtil.parseVersion("1.2.15-foo", "group", "artifact");
        
        assertEquals(expectedVersion, actualVersion);
    }

    @Test
    void parseVersion_WithSnapshotVersion_ReturnsVersionWithCorrectFullString() {
        Version version = VersionUtil.parseVersion("1.2.3-SNAPSHOT", "group", "artifact");
        
        assertEquals("group/artifact/1.2.3-SNAPSHOT", version.toFullString());
    }

    @Test
    void parseVersion_WithInvalidVersionString_ReturnsZeroVersion() {
        String invalidVersionString = "#M&+m@569P";
        
        Version version = VersionUtil.parseVersion(invalidVersionString, invalidVersionString, 
                "com.fasterxml.jackson.core.util.VersionUtil");

        assertEquals(0, version.getMajorVersion());
        assertEquals(0, version.getMinorVersion());
        assertEquals(0, version.getPatchLevel());
        assertFalse(version.isSnapshot());
        assertFalse(version.isUnknownVersion());
    }

    @Test
    void parseVersion_WithEmptyVersionString_ReturnsUnknownVersion() {
        Version version = VersionUtil.parseVersion("", "", "\"g2AT");
        
        assertTrue(version.isUnknownVersion());
    }

    @Test
    void parseVersion_WithNullVersionString_ReturnsNonSnapshotVersion() {
        Version version = VersionUtil.parseVersion(null, "/nUmRN)3", "");

        assertFalse(version.isSnapshot());
    }

    // Tests for versionFor method

    @Test
    void versionFor_WithKnownJacksonClass_ReturnsPackageVersion() {
        Version actualVersion = VersionUtil.versionFor(UTF8JsonGenerator.class);
        
        assertEquals(PackageVersion.VERSION, actualVersion);
    }

    /**
     * Test for issue #248: Ensure versionFor returns Version.unknownVersion() 
     * instead of null for classes without version information.
     */
    @Test
    void versionFor_WithClassWithoutVersionInfo_ReturnsUnknownVersion() {
        Version actualVersion = VersionUtil.versionFor(VersionUtilTest.class);
        
        assertEquals(Version.unknownVersion(), actualVersion);
    }

    // Tests for deprecated functionality

    @SuppressWarnings("deprecation")
    @Test
    void mavenVersionFor_WithValidParameters_ReturnsExpectedVersion() {
        Version expectedVersion = new Version(1, 2, 3, "SNAPSHOT", "foo.bar", "foo-bar");
        
        Version actualVersion = VersionUtil.mavenVersionFor(
                VersionUtilTest.class.getClassLoader(), 
                "foo.bar", 
                "foo-bar"
        );
        
        assertEquals(expectedVersion, actualVersion);
    }
}