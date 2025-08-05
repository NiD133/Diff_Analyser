package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.VersionUtil;

/**
 * Test suite for VersionUtil class functionality.
 * Tests version parsing, Maven version loading, and utility methods.
 */
public class VersionUtilTest {

    // ========== parseVersionPart() Tests ==========
    
    @Test
    public void parseVersionPart_shouldExtractLeadingDigitsFromComplexString() {
        // Given: A string starting with "0" followed by non-numeric characters
        String versionString = "0T*A{.*6/lD:1tm E_";
        
        // When: Parsing the version part
        int result = VersionUtil.parseVersionPart(versionString);
        
        // Then: Should extract only the leading "0"
        assertEquals(0, result);
    }

    @Test
    public void parseVersionPart_shouldExtractLeadingDigitFromStringWithSpecialChars() {
        // Given: A string starting with "9" followed by special characters
        String versionString = "9c*fdp6?ec}ur$$";
        
        // When: Parsing the version part
        int result = VersionUtil.parseVersionPart(versionString);
        
        // Then: Should extract only the leading "9"
        assertEquals(9, result);
    }

    @Test
    public void parseVersionPart_shouldReturnZeroForEmptyString() {
        // Given: An empty string
        String emptyString = "";
        
        // When: Parsing the version part
        int result = VersionUtil.parseVersionPart(emptyString);
        
        // Then: Should return 0
        assertEquals(0, result);
    }

    @Test
    public void parseVersionPart_shouldExtractFirstDigitFromMixedString() {
        // Given: A string starting with "3" followed by mixed characters
        String versionString = "3$Qtt~IU2x^1~fgh4]+";
        
        // When: Parsing the version part
        int result = VersionUtil.parseVersionPart(versionString);
        
        // Then: Should extract only the leading "3"
        assertEquals(3, result);
    }

    @Test(expected = NullPointerException.class)
    public void parseVersionPart_shouldThrowNullPointerExceptionForNullInput() {
        // Given: A null string
        String nullString = null;
        
        // When: Parsing the version part
        // Then: Should throw NullPointerException
        VersionUtil.parseVersionPart(nullString);
    }

    // ========== parseVersion() Tests ==========

    @Test
    public void parseVersion_shouldParseMajorVersionFromIdenticalStrings() {
        // Given: Identical version strings starting with "8"
        String versionString = "8juvlng,dux%Z/Gd4";
        
        // When: Parsing the version
        Version result = VersionUtil.parseVersion(versionString, versionString, versionString);
        
        // Then: Should extract major version 8, with minor and patch as 0
        assertEquals(8, result.getMajorVersion());
        assertEquals(0, result.getMinorVersion());
        assertEquals(0, result.getPatchLevel());
    }

    @Test
    public void parseVersion_shouldParseMinorVersionFromComplexString() {
        // Given: A version string containing "73" in the middle
        String versionString = "iQT[E/73*QBf";
        
        // When: Parsing the version
        Version result = VersionUtil.parseVersion(versionString, versionString, versionString);
        
        // Then: Should parse as major=0, minor=73, patch=0
        assertEquals(0, result.getMajorVersion());
        assertEquals(73, result.getMinorVersion());
        assertEquals(0, result.getPatchLevel());
    }

    @Test
    public void parseVersion_shouldDetectSnapshotVersion() {
        // Given: A long version string (simulating snapshot version)
        String longVersionString = "com.fasterxml.jackson.core.util.VersionUtil";
        
        // When: Parsing the version
        Version result = VersionUtil.parseVersion(longVersionString, longVersionString, longVersionString);
        
        // Then: Should be detected as snapshot version
        assertTrue("Should be detected as snapshot version", result.isSnapshot());
        assertEquals(0, result.getMinorVersion());
    }

    @Test
    public void parseVersion_shouldParsePatchVersionFromStringWithDash() {
        // Given: A version string ending with "-6"
        String versionString = "yf;Hr?-6";
        
        // When: Parsing the version
        Version result = VersionUtil.parseVersion(versionString, versionString, versionString);
        
        // Then: Should parse patch level as 6
        assertEquals(0, result.getMajorVersion());
        assertEquals(6, result.getPatchLevel());
    }

    @Test
    public void parseVersion_shouldReturnUnknownVersionForEmptyStrings() {
        // Given: Empty version strings
        String emptyString = "";
        
        // When: Parsing the version
        Version result = VersionUtil.parseVersion(emptyString, emptyString, emptyString);
        
        // Then: Should return unknown version
        assertTrue("Should be unknown version", result.isUknownVersion());
    }

    @Test
    public void parseVersion_shouldHandleDifferentGroupAndArtifactIds() {
        // Given: Different version, group, and artifact strings
        String versionString = "Ve";
        String groupId = "GR";
        String artifactId = "";
        
        // When: Parsing the version
        Version result = VersionUtil.parseVersion(versionString, groupId, artifactId);
        
        // Then: Should preserve group and artifact IDs
        assertEquals("GR", result.getGroupId());
        assertEquals("", result.getArtifactId());
        assertEquals(0, result.getMajorVersion());
        assertEquals(0, result.getMinorVersion());
        assertEquals(0, result.getPatchLevel());
    }

    @Test
    public void parseVersion_shouldHandleNullInputs() {
        // Given: Null inputs
        String nullString = null;
        
        // When: Parsing the version
        Version result = VersionUtil.parseVersion(nullString, nullString, nullString);
        
        // Then: Should handle gracefully with empty artifact ID
        assertEquals("", result.getArtifactId());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void parseVersion_shouldThrowExceptionForInvalidSeparator() {
        // Given: A string with only separator characters
        String invalidVersionString = ";";
        
        // When: Parsing the version
        // Then: Should throw ArrayIndexOutOfBoundsException
        VersionUtil.parseVersion(invalidVersionString, invalidVersionString, invalidVersionString);
    }

    // ========== Maven Version Tests ==========

    @Test(expected = NullPointerException.class)
    public void mavenVersionFor_shouldThrowExceptionForNullClassLoader() {
        // Given: Null class loader
        ClassLoader nullClassLoader = null;
        
        // When: Getting maven version
        // Then: Should throw NullPointerException
        VersionUtil.mavenVersionFor(nullClassLoader, "", "");
    }

    @Test
    public void mavenVersionFor_shouldReturnVersionForValidInputs() {
        // Given: System class loader and valid group/artifact IDs
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        String groupId = "[][.~r)|i/l/";
        String artifactId = "Ve";
        
        // When: Getting maven version
        Version result = VersionUtil.mavenVersionFor(systemClassLoader, groupId, artifactId);
        
        // Then: Should return a version (likely unknown for non-existent artifact)
        assertEquals(0, result.getMajorVersion());
    }

    // ========== Version Loading Tests ==========

    @Test
    public void versionFor_shouldReturnVersionForJsonFactoryClass() {
        // Given: JsonFactory class
        Class<JsonFactory> jsonFactoryClass = JsonFactory.class;
        
        // When: Getting version information
        Version result = VersionUtil.versionFor(jsonFactoryClass);
        
        // Then: Should return version information
        assertEquals(0, result.getPatchLevel());
    }

    @Test
    public void packageVersionFor_shouldReturnUnknownVersionForObjectClass() {
        // Given: Object class (which won't have version info)
        Class<Object> objectClass = Object.class;
        
        // When: Getting package version
        Version result = VersionUtil.packageVersionFor(objectClass);
        
        // Then: Should return unknown version
        assertTrue("Should be unknown version", result.isUknownVersion());
    }

    // ========== Utility Method Tests ==========

    @Test(expected = RuntimeException.class)
    public void throwInternal_shouldThrowRuntimeException() {
        // When: Calling throwInternal
        // Then: Should throw RuntimeException with specific message
        VersionUtil.throwInternal();
    }

    @Test(expected = RuntimeException.class)
    public void throwInternalReturnAny_shouldThrowRuntimeException() {
        // When: Calling throwInternalReturnAny
        // Then: Should throw RuntimeException with specific message
        VersionUtil.throwInternalReturnAny();
    }

    // ========== Instance Method Tests ==========

    @Test
    public void versionUtilInstance_shouldReturnNonSnapshotVersion() {
        // Given: A VersionUtil instance
        VersionUtil versionUtil = new VersionUtil();
        
        // When: Getting version
        Version result = versionUtil.version();
        
        // Then: Should return non-snapshot version
        assertFalse("Should not be snapshot version", result.isSnapshot());
    }
}