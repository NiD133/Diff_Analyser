package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.io.IOCase;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class IOCaseTest extends IOCase_ESTest_scaffolding {

    // Test for IOCase.INSENSITIVE checkIndexOf method
    @Test(timeout = 4000)
    public void testInsensitiveCheckIndexOf() throws Throwable {
        IOCase caseInsensitive = IOCase.INSENSITIVE;
        int index = caseInsensitive.checkIndexOf("1", 0, "1");
        assertEquals(0, index);
    }

    // Test for IOCase.INSENSITIVE checkCompareTo method
    @Test(timeout = 4000)
    public void testInsensitiveCheckCompareTo() throws Throwable {
        IOCase caseInsensitive = IOCase.INSENSITIVE;
        int comparisonResult = caseInsensitive.checkCompareTo("LINUX", "\"#S6?U_R7?'mwf");
        assertEquals(74, comparisonResult);
    }

    // Test for IOCase.value method with null value
    @Test(timeout = 4000)
    public void testValueWithNull() throws Throwable {
        IOCase defaultCase = IOCase.INSENSITIVE;
        IOCase resultCase = IOCase.value(null, defaultCase);
        assertEquals(IOCase.INSENSITIVE, resultCase);
    }

    // Test for IOCase.value method with non-null value
    @Test(timeout = 4000)
    public void testValueWithNonNull() throws Throwable {
        IOCase sensitiveCase = IOCase.SENSITIVE;
        IOCase insensitiveCase = IOCase.INSENSITIVE;
        IOCase resultCase = IOCase.value(sensitiveCase, insensitiveCase);
        assertEquals(IOCase.SENSITIVE, resultCase);
    }

    // Test for IOCase.values method
    @Test(timeout = 4000)
    public void testValues() throws Throwable {
        IOCase[] cases = IOCase.values();
        assertEquals(3, cases.length);
    }

    // Test for IOCase.valueOf method
    @Test(timeout = 4000)
    public void testValueOfSensitive() throws Throwable {
        IOCase sensitiveCase = IOCase.valueOf("SENSITIVE");
        assertEquals(IOCase.SENSITIVE, sensitiveCase);
    }

    @Test(timeout = 4000)
    public void testValueOfInsensitive() throws Throwable {
        IOCase insensitiveCase = IOCase.valueOf("INSENSITIVE");
        assertEquals("Insensitive", insensitiveCase.getName());
    }

    // Test for IOCase.isCaseSensitive method
    @Test(timeout = 4000)
    public void testIsCaseSensitive() throws Throwable {
        IOCase caseInsensitive = IOCase.INSENSITIVE;
        assertFalse(IOCase.isCaseSensitive(caseInsensitive));

        IOCase caseSensitive = IOCase.SENSITIVE;
        assertTrue(IOCase.isCaseSensitive(caseSensitive));

        IOCase caseSystem = IOCase.SYSTEM;
        assertTrue(IOCase.isCaseSensitive(caseSystem));

        assertFalse(IOCase.isCaseSensitive(null));
    }

    // Test for IOCase.forName method with valid and invalid names
    @Test(timeout = 4000)
    public void testForNameValid() throws Throwable {
        IOCase sensitiveCase = IOCase.forName("Sensitive");
        assertNotNull(sensitiveCase);
    }

    @Test(timeout = 4000)
    public void testForNameInvalid() throws Throwable {
        try {
            IOCase.forName("InvalidName");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.io.IOCase", e);
        }
    }

    // Test for IOCase check methods
    @Test(timeout = 4000)
    public void testCheckMethods() throws Throwable {
        IOCase caseSensitive = IOCase.SENSITIVE;
        assertTrue(caseSensitive.checkEquals("|,?])9N.", "|,?])9N."));
        assertFalse(caseSensitive.checkEquals("", "&b"));
        assertFalse(caseSensitive.checkRegionMatches("Sensitive", 258, "Insensitive"));
        assertTrue(caseSensitive.checkEndsWith("|,?])9N.", "|,?])9N."));
    }

    @Test(timeout = 4000)
    public void testCheckMethodsInsensitive() throws Throwable {
        IOCase caseInsensitive = IOCase.INSENSITIVE;
        assertTrue(caseInsensitive.checkEquals("System", "SYSTEM"));
        assertFalse(caseInsensitive.checkEquals("krmipp?jw", "1bq26gL^"));
        assertTrue(caseInsensitive.checkStartsWith("\"#s6?", "\"#s6?"));
        assertFalse(caseInsensitive.checkEndsWith("Sensitive", "gX@Wm5dEh2O"));
    }

    @Test(timeout = 4000)
    public void testCheckMethodsSystem() throws Throwable {
        IOCase caseSystem = IOCase.SYSTEM;
        assertFalse(caseSystem.checkStartsWith("vHnm-dNXF4", "2"));
        assertTrue(caseSystem.checkEquals("", ""));
    }

    // Test for IOCase toString method
    @Test(timeout = 4000)
    public void testToString() throws Throwable {
        IOCase caseInsensitive = IOCase.INSENSITIVE;
        assertEquals("Insensitive", caseInsensitive.toString());
    }
}