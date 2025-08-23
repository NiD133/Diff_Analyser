package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable, intention-revealing tests for VersionUtil.
 *
 * Notes:
 * - Inputs are chosen to exercise typical and edge parsing paths.
 * - Exception expectations are explicit and messages (when stable) are asserted.
 * - Avoids EvoSuite-specific scaffolding; uses plain JUnit 4.13 assertions.
 */
public class VersionUtilTest {

    // ---------------------------------------------------------------------
    // parseVersionPart(String)
    // ---------------------------------------------------------------------

    @Test
    public void parseVersionPart_returnsZero_whenEmpty() {
        assertEquals(0, VersionUtil.parseVersionPart(""));
    }

    @Test
    public void parseVersionPart_returnsZero_whenStartsWithZeroOrNonDigit() {
        // Starts with '0' -> value 0
        assertEquals(0, VersionUtil.parseVersionPart("0T*A{.*6/lD:1tm E_"));
    }

    @Test
    public void parseVersionPart_parsesLeadingDigits_only() {
        assertEquals(9, VersionUtil.parseVersionPart("9c*fdp6?ec}ur$$"));
        assertEquals(3, VersionUtil.parseVersionPart("3$Qtt~IU2x^1~fgh4]+"));
    }

    @Test
    public void parseVersionPart_throwsNPE_whenNull() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> VersionUtil.parseVersionPart(null));
        assertNull(ex.getMessage());
    }

    // ---------------------------------------------------------------------
    // parseVersion(String, groupId, artifactId)
    // ---------------------------------------------------------------------

    @Test
    public void parseVersion_parsesMajorMinorPatch_fromMixedTokens() {
        Version v = VersionUtil.parseVersion("8juvlng,dux%Z/Gd4", "8juvlng,dux%Z/Gd4", "8juvlng,dux%Z/Gd4");
        assertEquals(8, v.getMajorVersion());
        assertEquals(0, v.getMinorVersion());
        assertEquals(0, v.getPatchLevel());
    }

    @Test
    public void parseVersion_canFindMiddleNumber_asMinor() {
        Version v = VersionUtil.parseVersion("iQT[E/73*QBf", "iQT[E/73*QBf", "iQT[E/73*QBf");
        assertEquals(0, v.getMajorVersion());
        assertEquals(73, v.getMinorVersion());
        assertEquals(0, v.getPatchLevel());
    }

    @Test
    public void parseVersion_extractsPatch_fromTrailingNumber() {
        Version v = VersionUtil.parseVersion("yf;Hr?-6", "yf;Hr?-6", "yf;Hr?-6");
        assertEquals(0, v.getMajorVersion());
        assertEquals(0, v.getMinorVersion());
        assertEquals(6, v.getPatchLevel());
    }

    @Test
    public void parseVersion_returnsUnknown_forEmptyInput() {
        Version v = VersionUtil.parseVersion("", "", "");
        assertEquals(Version.unknownVersion(), v);
    }

    @Test
    public void parseVersion_setsGroupAndArtifactIds_whenProvided() {
        Version v = VersionUtil.parseVersion("Ve", "GR", "");
        assertEquals(0, v.getMajorVersion());
        assertEquals(0, v.getMinorVersion());
        assertEquals(0, v.getPatchLevel());
        assertEquals("GR", v.getGroupId());
        assertEquals("", v.getArtifactId());
    }

    @Test
    public void parseVersion_handlesNulls_gracefully() {
        Version v = VersionUtil.parseVersion(null, null, null);
        // Implementation-specific behavior: artifactId becomes empty string
        assertEquals("", v.getArtifactId());
    }

    @Test
    public void parseVersion_mayThrow_onPathologicalInput() {
        // Observed behavior in some implementations: splitting-only tokens can cause AIOOBE
        ArrayIndexOutOfBoundsException ex = assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> VersionUtil.parseVersion(";", ";", ";"));
        assertTrue(ex.getMessage() == null || ex.getMessage().contains("0"));
    }

    // ---------------------------------------------------------------------
    // versionFor(Class) and packageVersionFor(Class)
    // ---------------------------------------------------------------------

    @Test
    public void versionFor_returnsRealVersion_forJacksonClasses() {
        Version v = VersionUtil.versionFor(JsonFactory.class);
        assertNotNull(v);
        // Not asserting exact numbers, but keeping an observed invariant from original test
        assertEquals(0, v.getPatchLevel());
    }

    @Test
    public void packageVersionFor_returnsUnknown_forArbitraryClasses() {
        Version v = VersionUtil.packageVersionFor(Object.class);
        assertEquals(Version.unknownVersion(), v);
    }

    // ---------------------------------------------------------------------
    // mavenVersionFor(ClassLoader, groupId, artifactId)
    // ---------------------------------------------------------------------

    @Test
    public void mavenVersionFor_throwsNPE_whenClassLoaderIsNull() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> VersionUtil.mavenVersionFor(null, "", ""));
        assertNull(ex.getMessage());
    }

    @Test
    public void mavenVersionFor_returnsDefault_whenPomNotFound() {
        Version v = VersionUtil.mavenVersionFor(ClassLoader.getSystemClassLoader(), "[][.~r)|i/l/", "Ve");
        assertEquals(0, v.getMajorVersion());
    }

    // ---------------------------------------------------------------------
    // Internal error helpers
    // ---------------------------------------------------------------------

    @Test
    public void throwInternal_alwaysThrowsRuntimeException() {
        RuntimeException ex = assertThrows(RuntimeException.class, VersionUtil::throwInternal);
        assertTrue(ex.getMessage().contains("Internal error"));
    }

    @Test
    public void throwInternalReturnAny_alwaysThrowsRuntimeException() {
        RuntimeException ex = assertThrows(RuntimeException.class, VersionUtil::throwInternalReturnAny);
        assertTrue(ex.getMessage().contains("Internal error"));
    }

    // ---------------------------------------------------------------------
    // Instance API
    // ---------------------------------------------------------------------

    @Test
    public void instance_version_isNotSnapshot() {
        Version v = new VersionUtil().version();
        assertFalse(v.isSnapshot());
    }
}