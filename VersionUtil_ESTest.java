package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class VersionUtilTest extends VersionUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testParseVersionPartWithNonNumericPrefix() {
        int result = VersionUtil.parseVersionPart("0T*A{.*6/lD:1tm E_");
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testParseVersionPartWithNumericPrefix() {
        int result = VersionUtil.parseVersionPart("9c*fdp6?ec}ur$$");
        assertEquals(9, result);
    }

    @Test(timeout = 4000)
    public void testParseVersionPartWithEmptyString() {
        int result = VersionUtil.parseVersionPart("");
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testParseVersionPartWithComplexString() {
        int result = VersionUtil.parseVersionPart("3$Qtt~IU2x^1~fgh4]+");
        assertEquals(3, result);
    }

    @Test(timeout = 4000)
    public void testParseVersionWithComplexString() {
        Version version = VersionUtil.parseVersion("8juvlng,dux%Z/Gd4", "8juvlng,dux%Z/Gd4", "8juvlng,dux%Z/Gd4");
        assertEquals(0, version.getPatchLevel());
        assertEquals(0, version.getMinorVersion());
        assertEquals(8, version.getMajorVersion());
    }

    @Test(timeout = 4000)
    public void testParseVersionPartWithNullString() {
        try {
            VersionUtil.parseVersionPart(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.fasterxml.jackson.core.util.VersionUtil", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseVersionWithInvalidFormat() {
        try {
            VersionUtil.parseVersion(";", ";", ";");
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("com.fasterxml.jackson.core.util.VersionUtil", e);
        }
    }

    @Test(timeout = 4000)
    public void testMavenVersionForWithNullClassLoader() {
        try {
            VersionUtil.mavenVersionFor(null, "", "");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.fasterxml.jackson.core.util.VersionUtil", e);
        }
    }

    @Test(timeout = 4000)
    public void testVersionForJsonFactoryClass() {
        Version version = VersionUtil.versionFor(JsonFactory.class);
        assertEquals(0, version.getPatchLevel());
    }

    @Test(timeout = 4000)
    public void testParseVersionWithNumericMinorVersion() {
        Version version = VersionUtil.parseVersion("iQT[E/73*QBf", "iQT[E/73*QBf", "iQT[E/73*QBf");
        assertEquals(0, version.getMajorVersion());
        assertEquals(73, version.getMinorVersion());
        assertEquals(0, version.getPatchLevel());
    }

    @Test(timeout = 4000)
    public void testParseVersionWithSnapshot() {
        Version version = VersionUtil.parseVersion("com.fasterxml.jackson.core.util.VersionUtil", "com.fasterxml.jackson.core.util.VersionUtil", "com.fasterxml.jackson.core.util.VersionUtil");
        assertTrue(version.isSnapshot());
        assertEquals(0, version.getMinorVersion());
    }

    @Test(timeout = 4000)
    public void testParseVersionWithPatchLevel() {
        Version version = VersionUtil.parseVersion("yf;Hr?-6", "yf;Hr?-6", "yf;Hr?-6");
        assertEquals(6, version.getPatchLevel());
        assertEquals(0, version.getMajorVersion());
    }

    @Test(timeout = 4000)
    public void testParseVersionWithEmptyStrings() {
        Version version = VersionUtil.parseVersion("", "", "");
        assertTrue(version.isUknownVersion());
    }

    @Test(timeout = 4000)
    public void testParseVersionWithDifferentGroupAndArtifactId() {
        Version version = VersionUtil.parseVersion("Ve", "GR", "");
        assertEquals("", version.getArtifactId());
        assertEquals(0, version.getPatchLevel());
        assertEquals(0, version.getMajorVersion());
        assertEquals("GR", version.getGroupId());
        assertEquals(0, version.getMinorVersion());
    }

    @Test(timeout = 4000)
    public void testParseVersionWithNullValues() {
        Version version = VersionUtil.parseVersion(null, null, null);
        assertEquals("", version.getArtifactId());
    }

    @Test(timeout = 4000)
    public void testMavenVersionForWithValidClassLoader() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Version version = VersionUtil.mavenVersionFor(classLoader, "[][.~r)|i/l/", "Ve");
        assertEquals(0, version.getMajorVersion());
    }

    @Test(timeout = 4000)
    public void testThrowInternalReturnAny() {
        try {
            VersionUtil.throwInternalReturnAny();
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            verifyException("com.fasterxml.jackson.core.util.VersionUtil", e);
        }
    }

    @Test(timeout = 4000)
    public void testThrowInternal() {
        try {
            VersionUtil.throwInternal();
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            verifyException("com.fasterxml.jackson.core.util.VersionUtil", e);
        }
    }

    @Test(timeout = 4000)
    public void testPackageVersionForObjectClass() {
        Version version = VersionUtil.packageVersionFor(Object.class);
        assertTrue(version.isUknownVersion());
    }

    @Test(timeout = 4000)
    public void testVersionUtilVersionMethod() {
        VersionUtil versionUtil = new VersionUtil();
        Version version = versionUtil.version();
        assertFalse(version.isSnapshot());
    }
}