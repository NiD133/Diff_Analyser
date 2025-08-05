package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.gson.internal.JavaVersion;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for the JavaVersion class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class JavaVersion_ESTest extends JavaVersion_ESTest_scaffolding {

    /**
     * Test parsing a non-numeric Java version string.
     * Expecting it to return 0.
     */
    @Test(timeout = 4000)
    public void testParseNonNumericVersion() throws Throwable {
        int version = JavaVersion.parseMajorJavaVersion("0Q?");
        assertEquals(0, version);
    }

    /**
     * Test parsing a null Java version string.
     * Expecting a NullPointerException.
     */
    @Test(timeout = 4000)
    public void testParseNullVersion() throws Throwable {
        try {
            JavaVersion.parseMajorJavaVersion(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.internal.JavaVersion", e);
        }
    }

    /**
     * Test if the current Java version is 9 or later.
     * Expecting false for Java versions earlier than 9.
     */
    @Test(timeout = 4000)
    public void testIsJava9OrLater() throws Throwable {
        boolean isJava9OrLater = JavaVersion.isJava9OrLater();
        assertFalse(isJava9OrLater);
    }

    /**
     * Test parsing an empty Java version string.
     * Expecting it to return 6 as a default.
     */
    @Test(timeout = 4000)
    public void testParseEmptyVersionString() throws Throwable {
        int version = JavaVersion.parseMajorJavaVersion("");
        assertEquals(6, version);
    }

    /**
     * Test parsing a negative Java version string.
     * Expecting it to return the negative value.
     */
    @Test(timeout = 4000)
    public void testParseNegativeVersion() throws Throwable {
        int version = JavaVersion.parseMajorJavaVersion("-8");
        assertEquals(-8, version);
    }

    /**
     * Test parsing a complex Java version string starting with "1".
     * Expecting it to return 1.
     */
    @Test(timeout = 4000)
    public void testParseComplexVersionString() throws Throwable {
        int version = JavaVersion.parseMajorJavaVersion("1.&N<+EILs/Cn\",");
        assertEquals(1, version);
    }

    /**
     * Test parsing a simple Java version string "1".
     * Expecting it to return 1.
     */
    @Test(timeout = 4000)
    public void testParseSimpleVersionString() throws Throwable {
        int version = JavaVersion.parseMajorJavaVersion("1");
        assertEquals(1, version);
    }

    /**
     * Test getting the major Java version of the current JVM.
     * Expecting it to return 8.
     */
    @Test(timeout = 4000)
    public void testGetMajorJavaVersion() throws Throwable {
        int version = JavaVersion.getMajorJavaVersion();
        assertEquals(8, version);
    }
}